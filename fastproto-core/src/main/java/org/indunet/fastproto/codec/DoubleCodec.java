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
import org.indunet.fastproto.annotation.DoubleType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

/**
 * Codec for Double type.
 * This codec is responsible for encoding and decoding Double types.
 * It is used in conjunction with the DoubleType annotation.
 * The codec ensures that the Double value is correctly encoded and decoded.
 * If there are any issues during the encoding or decoding process, an exception is thrown.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class DoubleCodec implements Codec<Double> {
    @Override
    public Double decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(DoubleType.class);
            val order = context.getByteOrder(type::byteOrder);

            return inputStream.readDouble(type.offset(), order);
        } catch (IndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding double type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, Double value) {
        try {
            val type = context.getDataTypeAnnotation(DoubleType.class);
            val order = context.getByteOrder(type::byteOrder);

            outputStream.writeDouble(type.offset(), order, value);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding double type.", e);
        }
    }
}
