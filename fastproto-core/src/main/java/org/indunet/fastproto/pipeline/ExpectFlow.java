/*
 * Copyright 2019-2025 indunet.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.indunet.fastproto.pipeline;

import lombok.val;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Expect;
import org.indunet.fastproto.annotation.Expects;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.graph.Reference;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Expect flow.
 * <p>
 * On encode: write configured constants to specific offsets.
 * On decode: verify the on-wire values match configured constants, otherwise throw DecodingException.
 * </p>
 *
 * @author Deng Ran
 * @since 3.12.0
 */
public class ExpectFlow extends Pipeline<PipelineContext> {
    @Override
    public void process(PipelineContext context) {
        val graph = context.getGraph();
        if (graph == null) {
            this.forward(context);
            return;
        }

        List<Expect> expects = collect(graph.root().getProtocolClass(), graph);

        if (context.getOutputStream() != null) {
            for (Expect e : expects) {
                writeExpect(context, e);
            }
        } else if (context.getInputStream() != null) {
            byte[] bytes = context.getInputStream().toByteBuffer().toBytes();
            for (Expect e : expects) {
                verifyExpect(bytes, e);
            }
        }

        this.forward(context);
    }

    protected List<Expect> collect(Class<?> rootClass, org.indunet.fastproto.graph.Graph graph) {
        List<Expect> list = new ArrayList<>();
        graph.stream()
                .filter(r -> r.getReferenceType() == Reference.ReferenceType.CLASS)
                .map(Reference::getProtocolClass)
                .distinct()
                .forEach(clazz -> {
                    for (Field f : clazz.getDeclaredFields()) {
                        f.setAccessible(true);
                        if (f.isAnnotationPresent(Expect.class)) {
                            list.add(f.getAnnotation(Expect.class));
                        }
                        if (f.isAnnotationPresent(Expects.class)) {
                            for (Expect e : f.getAnnotation(Expects.class).value()) {
                                list.add(e);
                            }
                        }
                    }
                });
        return list;
    }

    protected void writeExpect(PipelineContext ctx, Expect e) {
        if (e.bytes().length > 0) {
            for (int i = 0; i < e.bytes().length; i++) {
                ctx.getOutputStream().writeUInt8(e.offset() + i, e.bytes()[i] & 0xFF);
            }
            return;
        }
        if (e.value() == Long.MIN_VALUE) {
            throw new EncodingException("Expect: neither bytes nor value provided.");
        }
        int sz = e.size();
        long v = e.value();
        switch (sz) {
            case 1:
                ctx.getOutputStream().writeUInt8(e.offset(), (int) v);
                break;
            case 2:
                ctx.getOutputStream().writeUInt16(e.offset(), e.byteOrder(), (int) v);
                break;
            case 4:
                ctx.getOutputStream().writeUInt32(e.offset(), e.byteOrder(), v);
                break;
            case 8:
                ctx.getOutputStream().writeUInt64(e.offset(), e.byteOrder(), toUnsignedBigInteger(v));
                break;
            default:
                throw new EncodingException("Unsupported expect size: " + sz);
        }
    }

    protected void verifyExpect(byte[] bytes, Expect e) {
        if (e.bytes().length > 0) {
            for (int i = 0; i < e.bytes().length; i++) {
                int b = bytes[e.offset() + i] & 0xFF;
                int exp = e.bytes()[i] & 0xFF;
                if (b != exp) {
                    throw new DecodingException("Expect bytes mismatch at offset " + (e.offset() + i));
                }
            }
            return;
        }
        if (e.value() == Long.MIN_VALUE) {
            throw new DecodingException("Expect: neither bytes nor value provided.");
        }
        long actual = readUnsigned(bytes, e.offset(), e.size(), e.byteOrder());
        if (actual != e.value()) {
            throw new DecodingException("Expect value mismatch at offset " + e.offset());
        }
    }

    protected static BigInteger toUnsignedBigInteger(long value) {
        if (value >= 0) return BigInteger.valueOf(value);
        return BigInteger.valueOf(value).add(BigInteger.ONE.shiftLeft(64));
    }

    protected static long readUnsigned(byte[] bytes, int offset, int size, ByteOrder order) {
        switch (size) {
            case 1:
                return bytes[offset] & 0xFFL;
            case 2:
                if (order == ByteOrder.LITTLE) {
                    return (bytes[offset] & 0xFF) | ((bytes[offset + 1] & 0xFF) << 8);
                } else {
                    return ((bytes[offset] & 0xFF) << 8) | (bytes[offset + 1] & 0xFF);
                }
            case 4:
                if (order == ByteOrder.LITTLE) {
                    return (bytes[offset] & 0xFFL) |
                            ((bytes[offset + 1] & 0xFFL) << 8) |
                            ((bytes[offset + 2] & 0xFFL) << 16) |
                            ((bytes[offset + 3] & 0xFFL) << 24);
                } else {
                    return ((bytes[offset] & 0xFFL) << 24) |
                            ((bytes[offset + 1] & 0xFFL) << 16) |
                            ((bytes[offset + 2] & 0xFFL) << 8) |
                            (bytes[offset + 3] & 0xFFL);
                }
            case 8:
                if (order == ByteOrder.LITTLE) {
                    return (bytes[offset] & 0xFFL) |
                            ((bytes[offset + 1] & 0xFFL) << 8) |
                            ((bytes[offset + 2] & 0xFFL) << 16) |
                            ((bytes[offset + 3] & 0xFFL) << 24) |
                            ((bytes[offset + 4] & 0xFFL) << 32) |
                            ((bytes[offset + 5] & 0xFFL) << 40) |
                            ((bytes[offset + 6] & 0xFFL) << 48) |
                            ((bytes[offset + 7] & 0xFFL) << 56);
                } else {
                    return ((bytes[offset] & 0xFFL) << 56) |
                            ((bytes[offset + 1] & 0xFFL) << 48) |
                            ((bytes[offset + 2] & 0xFFL) << 40) |
                            ((bytes[offset + 3] & 0xFFL) << 32) |
                            ((bytes[offset + 4] & 0xFFL) << 24) |
                            ((bytes[offset + 5] & 0xFFL) << 16) |
                            ((bytes[offset + 6] & 0xFFL) << 8) |
                            (bytes[offset + 7] & 0xFFL);
                }
            default:
                throw new DecodingException("Unsupported expect size: " + size);
        }
    }

    @Override
    public long getCode() {
        return 0;
    }
}


