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
import org.indunet.fastproto.annotation.UInt32ArrayType;
import org.indunet.fastproto.annotation.UInt32Type;
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
 * UInt32 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class UInt32ArrayCodec implements Codec<long[]> {
    @Override
    public long[] decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(UInt32ArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = inputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = inputStream.toByteBuffer().reverse(type.offset(), type.length() * UInt32Type.SIZE)  / UInt32Type.SIZE + 1;
            }

            return IntStream.range(0, l)
                    .mapToLong(i -> inputStream.readUInt32(o + i * UInt32Type.SIZE, order))
                    .toArray();
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding uint32 array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, long[] values) {
        try {
            val type = context.getDataTypeAnnotation(UInt32ArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = outputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = outputStream.toByteBuffer().reverse(type.offset(), type.length() * UInt32Type.SIZE)  / UInt32Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i ->
                            outputStream.writeUInt32(o + i * UInt32Type.SIZE, order, values[i]));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding uint32 array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Long[]> {
        @Override
        public Long[] decode(CodecContext context, ByteBufferInputStream inputStream) {
            return LongStream.of(UInt32ArrayCodec.this.decode(context, inputStream))
                    .mapToObj(Long::valueOf)
                    .toArray(Long[]::new);
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Long[] values) {
            val longs = Stream.of(values)
                    .mapToLong(i -> i.longValue())
                    .toArray();

            UInt32ArrayCodec.this.encode(context, outputStream, longs);
        }
    }

    public class CollectionCodec implements Codec<Collection<Long>> {
        @Override
        public Collection<Long> decode(CodecContext context, ByteBufferInputStream inputStream) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Long> collection = CollectionUtils.newInstance(type);

                Arrays.stream(UInt32ArrayCodec.this.decode(context, inputStream))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Collection<Long> collection) {
            UInt32ArrayCodec.this.encode(context, outputStream, collection.stream()
                    .mapToLong(Long::longValue)
                    .toArray());
        }
    }
}
