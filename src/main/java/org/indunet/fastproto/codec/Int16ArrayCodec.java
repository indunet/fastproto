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
import org.indunet.fastproto.ByteBuffer;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Int16ArrayType;
import org.indunet.fastproto.annotation.Int16Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Int16 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class Int16ArrayCodec implements Codec<int[]> {
    public int[] decode(byte[] bytes, int offset, int length, ByteOrder order) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * Int16Type.SIZE)  / Int16Type.SIZE + 1;
            }

            return IntStream.range(0, l)
                    .map(i -> CodecUtils.int16Type(bytes, o + i * Int16Type.SIZE, order))
                    .toArray();
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding int16 array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, ByteOrder order, int[] values) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * Int16Type.SIZE)  / Int16Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i -> CodecUtils.int16Type(bytes, o + i * Int16Type.SIZE, order, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding int16 array type.", e);
        }
    }

    @Override
    public int[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(Int16ArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        return this.decode(bytes, type.offset(), type.length(), order);
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, int[] values) {
        val type = context.getDataTypeAnnotation(Int16ArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        try {
            var l = type.length();

            if (l < 0) {
                l = buffer.reverse(type.offset(), type.length() * Int16Type.SIZE)  / Int16Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i ->
                            CodecUtils.int16Type(buffer, type.offset() + i * Int16Type.SIZE, order, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding int16 array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Integer[]> {
        @Override
        public Integer[] decode(CodecContext context, byte[] bytes) {
            return IntStream.of(Int16ArrayCodec.this.decode(context, bytes))
                    .mapToObj(Integer::valueOf)
                    .toArray(Integer[]::new);
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Integer[] values) {
            val ints = Stream.of(values)
                    .mapToInt(i -> i.intValue())
                    .toArray();

            Int16ArrayCodec.this.encode(context, buffer, ints);
        }
    }

    public class CollectionCodec implements Codec<Collection<Integer>> {
        @Override
        public Collection<Integer> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Integer> collection = CollectionUtils.newInstance(type);

                Arrays.stream(Int16ArrayCodec.this.decode(context, bytes))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Collection<Integer> collection) {
            Int16ArrayCodec.this.encode(context, buffer, collection.stream()
                    .mapToInt(Integer::intValue)
                    .toArray());
        }
    }
}
