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
import org.indunet.fastproto.annotation.Int64ArrayType;
import org.indunet.fastproto.annotation.Int64Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Codec for Int64 Array type.
 * This codec is responsible for encoding and decoding Int64 array types.
 * It provides support for both primitive Int64 array and wrapper Long array, as well as collections of Long.
 * It is used in conjunction with the Int64ArrayType annotation.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class Int64ArrayCodec implements Codec<long[]> {
    @Override
    public long[] decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(Int64ArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = inputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = inputStream.toByteBuffer().reverse(type.offset(), type.length() * Int64Type.SIZE)  / Int64Type.SIZE + 1;
            }

            return IntStream.range(0, l)
                    .mapToLong(i -> inputStream.readInt64(o + i * Int64Type.SIZE, order))
                    .toArray();
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding int32 array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, long[] values) {
        try {
            val type = context.getDataTypeAnnotation(Int64ArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = outputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = outputStream.toByteBuffer().reverse(type.offset(), type.length() * Int64Type.SIZE)  / Int64Type.SIZE + 1;
            }

            IntStream.range(0, Math.min(l, values.length))
                    .forEach(i -> outputStream.writeInt64(o + i * Int64Type.SIZE, order, values[i]));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding int32 array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Long[]> {
        @Override
        public Long[] decode(CodecContext context, ByteBufferInputStream inputStream) {
            return LongStream.of(Int64ArrayCodec.this.decode(context, inputStream))
                    .mapToObj(Long::valueOf)
                    .toArray(Long[]::new);
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Long[] values) {
            val longs = Stream.of(values)
                    .mapToLong(i -> i.longValue())
                    .toArray();

            Int64ArrayCodec.this.encode(context, outputStream, longs);
        }
    }

    public class CollectionCodec implements Codec<Collection<Long>> {
        @Override
        public Collection<Long> decode(CodecContext context, ByteBufferInputStream inputStream) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Long> collection = CollectionUtils.newInstance(type);

                Arrays.stream(Int64ArrayCodec.this.decode(context, inputStream))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Collection<Long> collection) {
            Int64ArrayCodec.this.encode(context, outputStream, collection.stream()
                    .mapToLong(Long::longValue)
                    .toArray());
        }
    }
}