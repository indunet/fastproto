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
import org.indunet.fastproto.annotation.BinaryType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Codec for handling byte array type.
 * This codec is responsible for encoding and decoding byte array types.
 * It provides support for both primitive byte array and wrapper Byte array, as well as collections of Byte.
 * It is used in conjunction with the BinaryType annotation.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class BinaryCodec implements Codec<byte[]> {
    @Override
    public byte[] decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(BinaryType.class);

            return inputStream.readBytes(type.offset(), type.length());
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding binary type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, byte[] values) {
        try {
            val type = context.getDataTypeAnnotation(BinaryType.class);
            val l = outputStream.toByteBuffer().reverse(type.offset(), type.length());

            outputStream.writeBytes(type.offset(), Arrays.copyOfRange(values, 0, l));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding binary type.", e);
        }
    }

    public class WrapperCodec implements Codec<Byte[]> {
        @Override
        public Byte[] decode(CodecContext context, ByteBufferInputStream inputStream) {
            val bs = BinaryCodec.this.decode(context, inputStream);
            val values = new Byte[bs.length];

            IntStream.range(0, bs.length)
                    .forEach(i -> values[i] = bs[i]);

            return values;
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Byte[] binary) {
            val bs = new byte[binary.length];

            IntStream.range(0, bs.length)
                    .forEach(i -> bs[i] = binary[i]);

            BinaryCodec.this.encode(context, outputStream, bs);
        }
    }

    public class CollectionCodec implements Codec<Collection<Byte>> {
        @Override
        public Collection<Byte> decode(CodecContext context, ByteBufferInputStream inputStream) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Byte> collection = CollectionUtils.newInstance(type);

                for (byte b : BinaryCodec.this.decode(context, inputStream)) {
                    collection.add(b);
                }

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Collection<Byte> collection) {
            val bs = new byte[collection.size()];
            val values = collection.stream()
                    .toArray(Byte[]::new);

            IntStream.range(0, bs.length)
                    .forEach(i -> bs[i] = values[i]);

            BinaryCodec.this.encode(context, outputStream, bs);
        }
    }
}
