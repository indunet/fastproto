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
import org.indunet.fastproto.ByteBuffer;
import org.indunet.fastproto.annotation.BinaryType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Byte array type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class BinaryCodec implements Codec<byte[]> {
    public byte[] decode(byte[] bytes, int offset, int length) {
        try {
            return CodecUtils.binaryType(bytes, offset, length);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding binary type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, byte[] values) {
        try {
            CodecUtils.binaryType(bytes, offset, length, values);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding binary type.", e);
        }
    }

    @Override
    public byte[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(BinaryType.class);

        return this.decode(bytes, type.offset(), type.length());
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, byte[] values) {
        val type = context.getDataTypeAnnotation(BinaryType.class);

        try {
            CodecUtils.binaryType(buffer, type.offset(), type.length(), values);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding binary type.", e);
        }
    }

    public class WrapperCodec implements Codec<Byte[]> {
        @Override
        public Byte[] decode(CodecContext context, byte[] bytes) {
            val bs = BinaryCodec.this.decode(context, bytes);
            val values = new Byte[bs.length];

            IntStream.range(0, bs.length)
                    .forEach(i -> values[i] = bs[i]);

            return values;
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Byte[] values) {
            val bs = new byte[values.length];

            IntStream.range(0, bs.length)
                    .forEach(i -> bs[i] = values[i]);

            BinaryCodec.this.encode(context, buffer, bs);
        }
    }

    public class CollectionCodec implements Codec<Collection<Byte>> {
        @Override
        public Collection<Byte> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Byte> collection = CollectionUtils.newInstance(type);

                for (byte b : BinaryCodec.this.decode(context, bytes)) {
                    collection.add(b);
                }

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Collection<Byte> collection) {
            val bs = new byte[collection.size()];
            val values = collection.stream()
                    .toArray(Byte[]::new);

            IntStream.range(0, bs.length)
                    .forEach(i -> bs[i] = values[i]);

            BinaryCodec.this.encode(context, buffer, bs);
        }
    }
}
