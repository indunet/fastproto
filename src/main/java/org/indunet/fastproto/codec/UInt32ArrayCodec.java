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
import org.indunet.fastproto.annotation.UInt32ArrayType;
import org.indunet.fastproto.annotation.UInt32Type;
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
 * UInt32 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class UInt32ArrayCodec implements Codec<long[]> {
    public long[] decode(byte[] bytes, int offset, int length, ByteOrder byteOrder) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * UInt32Type.SIZE)  / UInt32Type.SIZE + 1;
            }

            return IntStream.range(0, l)
                    .mapToLong(i -> CodecUtils.uint32Type(bytes, o + i * UInt32Type.SIZE, byteOrder))
                    .toArray();
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding uint32 array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, ByteOrder policy, long[] values) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * UInt32Type.SIZE)  / UInt32Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i -> CodecUtils.uint32Type(bytes, o + i * UInt32Type.SIZE, policy, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding uint32 array type.", e);
        }
    }

    @Override
    public long[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(UInt32ArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        return this.decode(bytes, type.offset(), type.length(), order);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, long[] value) {
        val type = context.getDataTypeAnnotation(UInt32ArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        this.encode(bytes, type.offset(), type.length(), order, value);
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, long[] values) {
        val type = context.getDataTypeAnnotation(UInt32ArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        try {
            var l = type.length();

            if (l < 0) {
                l = buffer.reverse(type.offset(), type.length() * UInt32Type.SIZE)  / UInt32Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i ->
                            CodecUtils.uint32Type(buffer, type.offset() + i * UInt32Type.SIZE, order, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding uint32 array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Long[]> {
        @Override
        public Long[] decode(CodecContext context, byte[] bytes) {
            return LongStream.of(UInt32ArrayCodec.this.decode(context, bytes))
                    .mapToObj(Long::valueOf)
                    .toArray(Long[]::new);
        }

        @Override
        public void encode(CodecContext context, byte[] bytes, Long[] values) {
            val longs = Stream.of(values)
                    .mapToLong(i -> i.longValue())
                    .toArray();

            UInt32ArrayCodec.this.encode(context, bytes, longs);
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Long[] values) {
            val longs = Stream.of(values)
                    .mapToLong(i -> i.longValue())
                    .toArray();

            UInt32ArrayCodec.this.encode(context, buffer, longs);
        }
    }

    public class CollectionCodec implements Codec<Collection<Long>> {
        @Override
        public Collection<Long> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Long> collection = CollectionUtils.newInstance(type);

                Arrays.stream(UInt32ArrayCodec.this.decode(context, bytes))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, byte[] bytes, Collection<Long> collection) {
            UInt32ArrayCodec.this.encode(context, bytes, collection.stream()
                    .mapToLong(Long::longValue)
                    .toArray());
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Collection<Long> collection) {
            UInt32ArrayCodec.this.encode(context, buffer, collection.stream()
                    .mapToLong(Long::longValue)
                    .toArray());
        }
    }
}
