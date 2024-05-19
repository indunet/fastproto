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
import org.indunet.fastproto.annotation.UInt8ArrayType;
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
 * Codec for UInt8 Array type.
 * This codec is responsible for encoding and decoding UInt8 array types.
 * It provides support for both primitive UInt8 array and wrapper Integer array, as well as collections of Integer.
 * It is used in conjunction with the UInt8ArrayType annotation.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class UInt8ArrayCodec implements Codec<int[]> {
    @Override
    public int[] decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(UInt8ArrayType.class);
            val o = inputStream.toByteBuffer().reverse(type.offset());
            val l = inputStream.toByteBuffer().reverse(type.offset(), type.length());

            return IntStream.range(0, l)
                    .map(i -> inputStream.readUInt8(o + i))
                    .toArray();
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding uint8 array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, int[] values) {
        try {
            val type = context.getDataTypeAnnotation(UInt8ArrayType.class);
            val o = outputStream.toByteBuffer().reverse(type.offset());
            val l = outputStream.toByteBuffer().reverse(type.offset(), type.length());

            IntStream.range(0, l)
                    .forEach(i -> outputStream.writeUInt8(o + i, values[i]));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding uint8 array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Integer[]> {
        @Override
        public Integer[] decode(CodecContext context, ByteBufferInputStream inputStream) {
            return IntStream.of(UInt8ArrayCodec.this.decode(context, inputStream))
                    .mapToObj(Integer::valueOf)
                    .toArray(Integer[]::new);
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Integer[] values) {
            val ints = Stream.of(values)
                    .mapToInt(i -> i.intValue())
                    .toArray();

            UInt8ArrayCodec.this.encode(context, outputStream, ints);
        }
    }

    public class CollectionCodec implements Codec<Collection<Integer>> {
        @Override
        public Collection<Integer> decode(CodecContext context, ByteBufferInputStream inputStream) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Integer> collection = CollectionUtils.newInstance(type);

                Arrays.stream(UInt8ArrayCodec.this.decode(context, inputStream))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Collection<Integer> collection) {
            UInt8ArrayCodec.this.encode(context, outputStream, collection.stream()
                    .mapToInt(Integer::intValue)
                    .toArray());
        }
    }
}