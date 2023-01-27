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
import org.indunet.fastproto.annotation.FloatArrayType;
import org.indunet.fastproto.annotation.FloatType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Float array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class FloatArrayCodec implements Codec<float[]> {
    public float[] decode(byte[] bytes, int offset, int length, ByteOrder policy) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * FloatType.SIZE)  / FloatType.SIZE + 1;
            }

            val values = new float[l];

            IntStream.range(0, l)
                .forEach(i -> values[i] = CodecUtils.floatType(bytes, o + i * FloatType.SIZE, policy));

            return values;
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding float array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, ByteOrder byteOrder, float[] values) {
        try {
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * FloatType.SIZE) / FloatType.SIZE + 1;
            }

            if (l >= values.length) {
                IntStream.range(0, values.length)
                        .forEach(i -> CodecUtils.floatType(bytes, offset + i * FloatType.SIZE, byteOrder, values[i]));
            } else {
                IntStream.range(0, l)
                        .forEach(i -> CodecUtils.floatType(bytes, offset + i * FloatType.SIZE, byteOrder, values[i]));
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding float array type.", e);
        }
    }

    @Override
    public float[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(FloatArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        return this.decode(bytes, type.offset(), type.length(), order);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, float[] value) {
        val type = context.getDataTypeAnnotation(FloatArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        this.encode(bytes, type.offset(), type.length(), order, value);
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, float[] values) {
        val type = context.getDataTypeAnnotation(FloatArrayType.class);
        val order = context.getByteOrder(type::byteOrder);

        try {
            var l = type.length();

            if (l < 0) {
                l = buffer.reverse(type.offset(), type.length() * FloatType.SIZE) / FloatType.SIZE + 1;
            }

            if (l >= values.length) {
                IntStream.range(0, values.length)
                        .forEach(i -> CodecUtils.floatType(buffer, type.offset() + i * FloatType.SIZE, order, values[i]));
            } else {
                IntStream.range(0, l)
                        .forEach(i -> CodecUtils.floatType(buffer, type.offset() + i * FloatType.SIZE, order, values[i]));
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding float array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Float[]> {
        @Override
        public Float[] decode(CodecContext context, byte[] bytes) {
            val floats = FloatArrayCodec.this.decode(context, bytes);
            val values = new Float[floats.length];

            IntStream.range(0, floats.length)
                    .forEach(i -> values[i] = floats[i]);

            return values;
        }

        @Override
        public void encode(CodecContext context, byte[] bytes, Float[] values) {
            val floats = new float[values.length];

            IntStream.range(0, floats.length)
                    .forEach(i -> floats[i] = values[i]);

            FloatArrayCodec.this.encode(context, bytes, floats);
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Float[] values) {
            val floats = new float[values.length];

            IntStream.range(0, floats.length)
                    .forEach(i -> floats[i] = values[i]);

            FloatArrayCodec.this.encode(context, buffer, floats);
        }
    }

    public class CollectionCodec implements Codec<Collection<Float>> {
        @Override
        public Collection<Float> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Float> collection = CollectionUtils.newInstance(type);

                for (float b: FloatArrayCodec.this.decode(context, bytes)) {
                    collection.add(b);
                }

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, byte[] bytes, Collection<Float> collection) {
            val bs = new float[collection.size()];
            val values = collection.stream()
                    .toArray(Float[]::new);

            IntStream.range(0, bs.length)
                    .forEach(i -> bs[i] = values[i]);

            FloatArrayCodec.this.encode(context, bytes, bs);
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Collection<Float> collection) {
            val bs = new float[collection.size()];
            val values = collection.stream()
                    .toArray(Float[]::new);

            IntStream.range(0, bs.length)
                    .forEach(i -> bs[i] = values[i]);

            FloatArrayCodec.this.encode(context, buffer, bs);
        }
    }
}
