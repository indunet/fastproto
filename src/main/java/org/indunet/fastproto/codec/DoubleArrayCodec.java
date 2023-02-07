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
import org.indunet.fastproto.annotation.DoubleArrayType;
import org.indunet.fastproto.annotation.DoubleType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Double array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class DoubleArrayCodec implements Codec<double[]> {
    @Override
    public double[] decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(DoubleArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = inputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = inputStream.toByteBuffer().reverse(type.offset(), type.length() * DoubleType.SIZE)  / DoubleType.SIZE + 1;
            }

            return IntStream.range(0, l)
                    .mapToDouble(i -> inputStream.readDouble(o + i * DoubleType.SIZE, order))
                    .toArray();
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding double array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, double[] values) {
        try {
            val type = context.getDataTypeAnnotation(DoubleArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = outputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = outputStream.toByteBuffer().reverse(type.offset(), type.length() * DoubleType.SIZE) / DoubleType.SIZE + 1;
            }

            IntStream.range(0, Math.min(l, values.length))
                    .forEach(i -> outputStream.writeDouble(o + i * DoubleType.SIZE, order, values[i]));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding double array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Double[]> {
        @Override
        public Double[] decode(CodecContext context, ByteBufferInputStream inputStream) {
            return DoubleStream.of(DoubleArrayCodec.this.decode(context, inputStream))
                    .mapToObj(Double::valueOf)
                    .toArray(Double[]::new);
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Double[] values) {
            val doubles = Stream.of(values)
                    .mapToDouble(i -> i.doubleValue())
                    .toArray();

            DoubleArrayCodec.this.encode(context, outputStream, doubles);
        }
    }

    public class CollectionCodec implements Codec<Collection<Double>> {
        @Override
        public Collection<Double> decode(CodecContext context, ByteBufferInputStream inputStream) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Double> collection = CollectionUtils.newInstance(type);

                Arrays.stream(DoubleArrayCodec.this.decode(context, inputStream))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Collection<Double> collection) {
            val doubles = collection.stream()
                    .mapToDouble(Double::doubleValue)
                    .toArray();

            DoubleArrayCodec.this.encode(context, outputStream, doubles);
        }
    }
}
