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
import org.indunet.fastproto.annotation.Int64ArrayType;
import org.indunet.fastproto.annotation.Int64Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Int64 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class Int64ArrayCodec implements Codec<long[]> {
    public long[] decode(byte[] bytes, int offset, int length, ByteOrder policy) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * Int64Type.SIZE)  / Int64Type.SIZE + 1;
            }

            return IntStream.range(0, l)
                    .mapToLong(i -> CodecUtils.int64Type(bytes, o + i * Int64Type.SIZE, policy))
                    .toArray();
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding int32 array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, ByteOrder policy, long[] values) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * Int64Type.SIZE)  / Int64Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i -> CodecUtils.int64Type(bytes, o + i * Int64Type.SIZE, policy, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding int32 array type.", e);
        }
    }

    @Override
    public long[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(Int64ArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        return this.decode(bytes, type.offset(), type.length(), order);
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, long[] values) {
        val type = context.getDataTypeAnnotation(Int64ArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        try {
            var l = type.length();

            if (l < 0) {
                l = buffer.reverse(type.offset(), type.length() * Int64Type.SIZE)  / Int64Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i ->
                            CodecUtils.int64Type(buffer, type.offset() + i * Int64Type.SIZE, order, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding int32 array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Long[]> {
        @Override
        public Long[] decode(CodecContext context, byte[] bytes) {
            return LongStream.of(Int64ArrayCodec.this.decode(context, bytes))
                    .mapToObj(Long::valueOf)
                    .toArray(Long[]::new);
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Long[] values) {
            val longs = Stream.of(values)
                    .mapToLong(i -> i.longValue())
                    .toArray();

            Int64ArrayCodec.this.encode(context, buffer, longs);
        }
    }

    public class CollectionCodec implements Codec<Collection<Long>> {
        @Override
        public Collection<Long> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Long> collection = CollectionUtils.newInstance(type);

                Arrays.stream(Int64ArrayCodec.this.decode(context, bytes))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Collection<Long> collection) {
            Int64ArrayCodec.this.encode(context, buffer, collection.stream()
                    .mapToLong(Long::longValue)
                    .toArray());
        }
    }
}