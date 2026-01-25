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
import org.indunet.fastproto.annotation.Int16ArrayType;
import org.indunet.fastproto.annotation.Int16Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Codec for Short Array type.
 * This codec is responsible for encoding and decoding Short array types.
 * It provides support for both primitive Short array and wrapper Short array, as well as collections of Short.
 * It is used in conjunction with the Int16ArrayType annotation.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class ShortArrayCodec implements Codec<short[]> {
    @Override
    public short[] decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(Int16ArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = inputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = inputStream.toByteBuffer().reverse(type.offset(), type.length() * Int16Type.SIZE)  / Int16Type.SIZE + 1;
            }

            val values = new short[l];

            IntStream.range(0, l)
                    .forEach(i -> values[i] = inputStream.readShort(o + i * Int16Type.SIZE, order));

            return values;
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding short array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, short[] values) {
        try {
            val type = context.getDataTypeAnnotation(Int16ArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = outputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = outputStream.toByteBuffer().reverse(type.offset(), type.length() * Int16Type.SIZE)  / Int16Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i -> outputStream.writeShort(o + i * Int16Type.SIZE, order, values[i]));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding short array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Short[]> {
        @Override
        public Short[] decode(CodecContext context, ByteBufferInputStream inputStream) {
            val shorts = ShortArrayCodec.this.decode(context, inputStream);
            val values = new Short[shorts.length];

            IntStream.range(0, shorts.length)
                    .forEach(i -> values[i] = shorts[i]);

            return values;
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Short[] values) {
            val shorts = new short[values.length];

            IntStream.range(0, shorts.length)
                    .forEach(i -> shorts[i] = values[i]);

            ShortArrayCodec.this.encode(context, outputStream, shorts);
        }
    }

    public class CollectionCodec implements Codec<Collection<Short>> {
        @Override
        public Collection<Short> decode(CodecContext context, ByteBufferInputStream inputStream) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Short> collection = CollectionUtils.newInstance(type);

                for (short b: ShortArrayCodec.this.decode(context, inputStream)) {
                    collection.add(b);
                }

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Collection<Short> collection) {
            val ss = new short[collection.size()];
            val values = collection.stream()
                    .toArray(Short[]::new);

            IntStream.range(0, ss.length)
                    .forEach(i -> ss[i] = values[i]);

            ShortArrayCodec.this.encode(context, outputStream, ss);
        }
    }
}
