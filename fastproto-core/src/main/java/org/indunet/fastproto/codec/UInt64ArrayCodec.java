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
import org.indunet.fastproto.annotation.UInt64ArrayType;
import org.indunet.fastproto.annotation.UInt64Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.CollectionUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Codec for UInt64 Array type.
 * This codec is responsible for encoding and decoding UInt64 array types.
 * It provides support for both primitive UInt64 array and wrapper BigInteger array, as well as collections of BigInteger.
 * It is used in conjunction with the UInt64ArrayType annotation.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class UInt64ArrayCodec implements Codec<BigInteger[]> {
    @Override
    public BigInteger[] decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(UInt64ArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = inputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = inputStream.toByteBuffer().reverse(type.offset(), type.length() * UInt64Type.SIZE)  / UInt64Type.SIZE + 1;
            }

            return IntStream.range(0, l)
                    .mapToObj(i -> inputStream.readUInt64(o + i * UInt64Type.SIZE, order))
                    .toArray(BigInteger[]::new);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding uint64 array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, BigInteger[] values) {
        try {
            val type = context.getDataTypeAnnotation(UInt64ArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = outputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = outputStream.toByteBuffer().reverse(type.offset(), type.length() * UInt64Type.SIZE)  / UInt64Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i -> outputStream.writeUInt64(o + i * UInt64Type.SIZE, order, values[i]));
        } catch (IndexOutOfBoundsException | IllegalArgumentException | NullPointerException e) {
            throw new EncodingException("Fail encoding uint64 array type.", e);
        }
    }

    public class CollectionCodec implements Codec<Collection<BigInteger>> {
        @Override
        public Collection<BigInteger> decode(CodecContext context, ByteBufferInputStream inputStream) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<BigInteger> collection = CollectionUtils.newInstance(type);

                Arrays.stream(UInt64ArrayCodec.this.decode(context, inputStream))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Collection<BigInteger> collection) {
            UInt64ArrayCodec.this.encode(context, outputStream, collection.stream()
                    .toArray(BigInteger[]::new));
        }
    }
}