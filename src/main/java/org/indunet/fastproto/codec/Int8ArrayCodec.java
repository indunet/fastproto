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
import org.indunet.fastproto.io.ByteBuffer;
import org.indunet.fastproto.annotation.Int8ArrayType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Int8 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class Int8ArrayCodec implements Codec<int[]> {
    @Override
    public int[] decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(Int8ArrayType.class);
            val o = inputStream.toByteBuffer().reverse(type.offset());
            val l = inputStream.toByteBuffer().reverse(type.offset(), type.length());

            return IntStream.range(0, l)
                    .map(i -> inputStream.readInt8(o + i))
                    .toArray();
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding int8 array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, int[] values) {
        val type = context.getDataTypeAnnotation(Int8ArrayType.class);

        try {
            val o = outputStream.toByteBuffer().reverse(type.offset());
            val l = outputStream.toByteBuffer().reverse(type.offset(), type.length());

            IntStream.range(0, l)
                    .forEach(i -> outputStream.writeInt8(o + i, values[i]));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding int8 array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Integer[]> {
        @Override
        public Integer[] decode(CodecContext context, byte[] bytes) {
            return IntStream.of(Int8ArrayCodec.this.decode(context, bytes))
                    .mapToObj(Integer::valueOf)
                    .toArray(Integer[]::new);
        }

        @Override
        public Integer[] decode(CodecContext context, ByteBufferInputStream inputStream) {
            return IntStream.of(Int8ArrayCodec.this.decode(context, inputStream))
                    .mapToObj(Integer::valueOf)
                    .toArray(Integer[]::new);
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Integer[] values) {
            val ints = Stream.of(values)
                    .mapToInt(i -> i.intValue())
                    .toArray();

            Int8ArrayCodec.this.encode(context, buffer, ints);
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Integer[] values) {
            val ints = Stream.of(values)
                    .mapToInt(i -> i.intValue())
                    .toArray();

            Int8ArrayCodec.this.encode(context, outputStream, ints);
        }
    }

    public class CollectionCodec implements Codec<Collection<Integer>> {
        @Override
        public Collection<Integer> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Integer> collection = CollectionUtils.newInstance(type);

                Arrays.stream(Int8ArrayCodec.this.decode(context, bytes))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public Collection<Integer> decode(CodecContext context, ByteBufferInputStream inputStream) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Integer> collection = CollectionUtils.newInstance(type);

                Arrays.stream(Int8ArrayCodec.this.decode(context, inputStream))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Collection<Integer> collection) {
            Int8ArrayCodec.this.encode(context, buffer, collection.stream()
                    .mapToInt(Integer::intValue)
                    .toArray());
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Collection<Integer> collection) {
            Int8ArrayCodec.this.encode(context, outputStream, collection.stream()
                    .mapToInt(Integer::intValue)
                    .toArray());
        }
    }
}
