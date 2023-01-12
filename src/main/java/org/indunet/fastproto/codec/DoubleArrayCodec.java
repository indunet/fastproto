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
import org.indunet.fastproto.annotation.DoubleArrayType;
import org.indunet.fastproto.annotation.DoubleType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.*;
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
    public double[] decode(byte[] bytes, int offset, int length, ByteOrder byteOrder) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * DoubleType.SIZE)  / DoubleType.SIZE + 1;
            }

            return IntStream.range(0, l)
                    .mapToDouble(i -> CodecUtils.doubleType(bytes, o + i * DoubleType.SIZE, byteOrder))
                    .toArray();
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding double array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, ByteOrder byteOrder, double[] values) {
        try {
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * DoubleType.SIZE) / DoubleType.SIZE + 1;
            }

            if (l >= values.length) {
                IntStream.range(0, values.length)
                        .forEach(i -> CodecUtils.doubleType(bytes, offset + i * DoubleType.SIZE, byteOrder, values[i]));
            } else {
                IntStream.range(0, l)
                        .forEach(i -> CodecUtils.doubleType(bytes, offset + i * DoubleType.SIZE, byteOrder, values[i]));
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding double array type.", e);
        }
    }

    @Override
    public double[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(DoubleArrayType.class);
        val byteOrder = Arrays.stream(type.byteOrder())
                .findFirst()
                .orElseGet(context::getDefaultByteOrder);

        return this.decode(bytes, type.offset(), type.length(), byteOrder);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, double[] value) {
        val type = context.getDataTypeAnnotation(DoubleArrayType.class);
        val byteOrder = Arrays.stream(type.byteOrder())
                .findFirst()
                .orElseGet(context::getDefaultByteOrder);

        this.encode(bytes, type.offset(), type.length(), byteOrder, value);
    }

    public class WrapperCodec implements Codec<Double[]> {
        @Override
        public Double[] decode(CodecContext context, byte[] bytes) {
            return DoubleStream.of(DoubleArrayCodec.this.decode(context, bytes))
                    .mapToObj(Double::valueOf)
                    .toArray(Double[]::new);
        }

        @Override
        public void encode(CodecContext context, byte[] bytes, Double[] values) {
            val doubles = Stream.of(values)
                    .mapToDouble(i -> i.doubleValue())
                    .toArray();

            DoubleArrayCodec.this.encode(context, bytes, doubles);
        }
    }

    public class CollectionCodec implements Codec<Collection<Double>> {
        @Override
        public Collection<Double> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Double> collection = CollectionUtils.newInstance(type);

                Arrays.stream(DoubleArrayCodec.this.decode(context, bytes))
                        .forEach(collection::add);

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, byte[] bytes, Collection<Double> collection) {
            val doubles = collection.stream()
                    .mapToDouble(Double::doubleValue)
                    .toArray();

            DoubleArrayCodec.this.encode(context, bytes, doubles);
        }
    }
}
