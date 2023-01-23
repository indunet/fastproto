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
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Int16ArrayType;
import org.indunet.fastproto.annotation.Int16Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Short array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class ShortArrayCodec implements Codec<short[]> {
    public short[] decode(byte[] bytes, int offset, int length, ByteOrder policy) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * Int16Type.SIZE)  / Int16Type.SIZE + 1;
            }

            val values = new short[l];

            IntStream.range(0, l)
                    .forEach(i -> values[i] = CodecUtils.shortType(bytes, o + i * Int16Type.SIZE, policy));

            return values;
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding short array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, ByteOrder policy, short[] values) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * Int16Type.SIZE)  / Int16Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i -> CodecUtils.shortType(bytes, o + i * Int16Type.SIZE, policy, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding short array type.", e);
        }
    }

    @Override
    public short[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(Int16ArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        return this.decode(bytes, type.offset(), type.length(), order);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, short[] value) {
        val type = context.getDataTypeAnnotation(Int16ArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        this.encode(bytes, type.offset(), type.length(), order, value);
    }

    public class WrapperCodec implements Codec<Short[]> {
        @Override
        public Short[] decode(CodecContext context, byte[] bytes) {
            val shorts = ShortArrayCodec.this.decode(context, bytes);
            val values = new Short[shorts.length];

            IntStream.range(0, shorts.length)
                    .forEach(i -> values[i] = shorts[i]);

            return values;
        }

        @Override
        public void encode(CodecContext context, byte[] bytes, Short[] values) {
            val shorts = new short[values.length];

            IntStream.range(0, shorts.length)
                    .forEach(i -> shorts[i] = values[i]);

            ShortArrayCodec.this.encode(context, bytes, shorts);
        }
    }

    public class CollectionCodec implements Codec<Collection<Short>> {
        @Override
        public Collection<Short> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Short> collection = CollectionUtils.newInstance(type);

                for (short b: ShortArrayCodec.this.decode(context, bytes)) {
                    collection.add(b);
                }

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, byte[] bytes, Collection<Short> collection) {
            val ss = new short[collection.size()];
            val values = collection.stream()
                    .toArray(Short[]::new);

            IntStream.range(0, ss.length)
                    .forEach(i -> ss[i] = values[i]);

            ShortArrayCodec.this.encode(context, bytes, ss);
        }
    }
}
