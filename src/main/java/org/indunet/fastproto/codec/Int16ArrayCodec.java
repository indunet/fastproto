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

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Codec for Int16 Array type.
 * This codec is responsible for encoding and decoding Int16 array types.
 * It provides support for both primitive Int16 array and wrapper Integer array, as well as collections of Integer.
 * It is used in conjunction with the Int16ArrayType annotation.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class Int16ArrayCodec implements Codec<int[]> {
    @Override
    public int[] decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(Int16ArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = inputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = inputStream.toByteBuffer().reverse(type.offset(), type.length() * Int16Type.SIZE)  / Int16Type.SIZE + 1;
            }

            return IntStream.range(0, l)
                    .map(i -> inputStream.readInt16(o + i * Int16Type.SIZE, order))
                    .toArray();
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding int16 array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, int[] values) {
        try {
            val type = context.getDataTypeAnnotation(Int16ArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = outputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = outputStream.toByteBuffer().reverse(type.offset(), type.length() * Int16Type.SIZE)  / Int16Type.SIZE + 1;
            }

            IntStream.range(0, Math.min(l, values.length))
                    .forEach(i -> outputStream.writeInt16(o + i * Int16Type.SIZE, order, values[i]));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding int16 array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Integer[]> {
        @Override
        public Integer[] decode(CodecContext context, ByteBufferInputStream inputStream) {
            return IntStream.of(Int16ArrayCodec.this.decode(context, inputStream))
                    .mapToObj(Integer::valueOf)
                    .toArray(Integer[]::new);
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Integer[] values) {
            val ints = Stream.of(values)
                    .mapToInt(i -> i.intValue())
                    .toArray();

            Int16ArrayCodec.this.encode(context, outputStream, ints);
        }
    }

    public class CollectionCodec implements Codec<Collection<Integer>> {
        @Override
        public Collection<Integer> decode(CodecContext context, ByteBufferInputStream inputStream) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Integer> collection = CollectionUtils.newInstance(type);

                Arrays.stream(Int16ArrayCodec.this.decode(context, inputStream))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Collection<Integer> collection) {
            Int16ArrayCodec.this.encode(context, outputStream, collection.stream()
                    .mapToInt(Integer::intValue)
                    .toArray());
        }
    }
}
