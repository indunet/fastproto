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
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.checksum.ChecksumUtils;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.graph.Reference;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Checksum flow.
 * <p>
 * Executes after encode/decode flows to calculate or verify checksums declared via {@link Checksum}.
 * Supports CRC8/16/32/64 families and light-weight checks (XOR/LRC) per annotation parameters.
 * </p>
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public class ChecksumFlow extends Pipeline<PipelineContext> {
    @Override
    public void process(PipelineContext context) {
        val graph = context.getGraph();
        if (graph == null) {
            this.forward(context);
            return;
        }

        // Collect all fields annotated with @Checksum from all protocol classes in the graph
        List<Field> checksumFields = graph.stream()
                .filter(r -> r.getReferenceType() == Reference.ReferenceType.CLASS)
                .map(Reference::getProtocolClass)
                .distinct()
                .flatMap(clazz -> {
                    Field[] fs = clazz.getDeclaredFields();
                    for (Field f : fs) {
                        f.setAccessible(true);
                    }
                    return java.util.Arrays.stream(fs);
                })
                .filter(f -> f.isAnnotationPresent(Checksum.class))
                .collect(Collectors.toCollection(ArrayList::new));

        if (context.getOutputStream() != null) {
            byte[] bytes = context.getOutputStream().toByteBuffer().toBytes();
            for (Field f : checksumFields) {
                Checksum c = f.getAnnotation(Checksum.class);
                long crc = ChecksumUtils.calculate(bytes, c.start(), c.length(), c.type());
                writeChecksum(context, c.offset(), crc, c);
            }
        } else if (context.getInputStream() != null) {
            byte[] bytes = context.getInputStream().toByteBuffer().toBytes();
            for (Field f : checksumFields) {
                Checksum c = f.getAnnotation(Checksum.class);
                long crc = ChecksumUtils.calculate(bytes, c.start(), c.length(), c.type());
                long actual = readChecksum(bytes, c.offset(), c);
                if (actual != crc) {
                    throw new DecodingException("Checksum validation failed");
                }
            }
        }
        this.forward(context);
    }

    private static BigInteger toUnsignedBigInteger(long value) {
        if (value >= 0) return BigInteger.valueOf(value);
        return BigInteger.valueOf(value).add(BigInteger.ONE.shiftLeft(64));
    }

    private void writeChecksum(PipelineContext ctx, int offset, long crc, Checksum c) {
        switch (c.type()) {
            case CRC8:
            case CRC8_SMBUS:
            case CRC8_MAXIM:
            case XOR8:
            case LRC8:
                ctx.getOutputStream().writeUInt8(offset, (int) crc);
                break;
            case CRC16:
            case CRC16_CCITT:
            case CRC16_MODBUS:
                ctx.getOutputStream().writeUInt16(offset, c.byteOrder(), (int) crc);
                break;
            case CRC32:
            case CRC32C:
                ctx.getOutputStream().writeUInt32(offset, c.byteOrder(), crc);
                break;
            case CRC64_ECMA182:
            case CRC64_ISO:
                ctx.getOutputStream().writeUInt64(offset, c.byteOrder(), toUnsignedBigInteger(crc));
                break;
        }
    }

    private long readChecksum(byte[] bytes, int offset, Checksum c) {
        switch (c.type()) {
            case CRC8:
            case CRC8_SMBUS:
            case CRC8_MAXIM:
            case XOR8:
            case LRC8:
                return bytes[offset] & 0xFFL;
            case CRC16:
            case CRC16_CCITT:
            case CRC16_MODBUS:
                if (c.byteOrder() == ByteOrder.LITTLE) {
                    return (bytes[offset] & 0xFF) | ((bytes[offset + 1] & 0xFF) << 8);
                } else {
                    return ((bytes[offset] & 0xFF) << 8) | (bytes[offset + 1] & 0xFF);
                }
            case CRC32:
            case CRC32C:
                if (c.byteOrder() == ByteOrder.LITTLE) {
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
            case CRC64_ECMA182:
            case CRC64_ISO:
                if (c.byteOrder() == ByteOrder.LITTLE) {
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
        }
        return 0;
    }

    @Override
    public long getCode() {
        return 0;
    }
}
