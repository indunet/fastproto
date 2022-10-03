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
import org.indunet.fastproto.annotation.type.FloatArrayType;
import org.indunet.fastproto.annotation.type.FloatType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.util.stream.IntStream;

/**
 * Float array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class FloatArrayCodec implements Codec<float[]> {
    public float[] decode(byte[] bytes, int offset, int length) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * FloatType.SIZE)  / FloatType.SIZE + 1;
            }

            val values = new float[l];

            IntStream.range(0, l)
                .forEach(i -> values[i] = CodecUtils.floatType(bytes, o + i * FloatType.SIZE));

            return values;
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding float array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, float[] values) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * FloatType.SIZE) / FloatType.SIZE + 1;
            }

            if (o + l * FloatType.SIZE > values.length * FloatType.SIZE) {
                throw new IllegalArgumentException("Out of the datagram range.");
            } else if (l >= values.length) {
                IntStream.range(0, values.length)
                        .forEach(i -> CodecUtils.floatType(bytes, offset + i * FloatType.SIZE, values[i]));
            } else {
                IntStream.range(0, l)
                        .forEach(i -> CodecUtils.floatType(bytes, offset + i * FloatType.SIZE, values[i]));
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding float array type.", e);
        }
    }

    @Override
    public float[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(FloatArrayType.class);

        return this.decode(bytes, type.offset(), type.length());
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, float[] value) {
        val type = context.getDataTypeAnnotation(FloatArrayType.class);

        this.encode(bytes, type.offset(), type.length(), value);
    }
}
