/*
 * Copyright 2019-2022 indunet.org
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

package org.indunet.fastproto.codec;

import lombok.val;
import lombok.var;
import org.indunet.fastproto.io.ByteBuffer;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.UInt64ArrayType;
import org.indunet.fastproto.annotation.UInt64Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.CollectionUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * UInt64 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class UInt64ArrayCodec implements Codec<BigInteger[]> {
    public BigInteger[] decode(byte[] bytes, int offset, int length, ByteOrder byteOrder) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * UInt64Type.SIZE)  / UInt64Type.SIZE + 1;
            }

            return IntStream.range(0, l)
                    .mapToObj(i -> CodecUtils.uint64Type(bytes, o + i * UInt64Type.SIZE, byteOrder))
                    .toArray(BigInteger[]::new);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding uint64 array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, ByteOrder policy, BigInteger[] values) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * UInt64Type.SIZE)  / UInt64Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i -> CodecUtils.uint64Type(bytes, o + i * UInt64Type.SIZE, policy, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException e) {
            throw new EncodingException("Fail encoding uint64 array type.", e);
        }
    }

    @Override
    public BigInteger[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(UInt64ArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        return this.decode(bytes, type.offset(), type.length(), order);
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, BigInteger[] values) {
        val type = context.getDataTypeAnnotation(UInt64ArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        try {
            var l = type.length();

            if (l < 0) {
                l = buffer.reverse(type.offset(), type.length() * UInt64Type.SIZE)  / UInt64Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i -> CodecUtils.uint64Type(buffer, type.offset() + i * UInt64Type.SIZE, order, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException e) {
            throw new EncodingException("Fail encoding uint64 array type.", e);
        }
    }

    public class CollectionCodec implements Codec<Collection<BigInteger>> {
        @Override
        public Collection<BigInteger> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<BigInteger> collection = CollectionUtils.newInstance(type);

                Arrays.stream(UInt64ArrayCodec.this.decode(context, bytes))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Collection<BigInteger> collection) {
            UInt64ArrayCodec.this.encode(context, buffer, collection.stream()
                    .toArray(BigInteger[]::new));
        }
    }
}