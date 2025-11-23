/*
 * Copyright 2019-2025 indunet.org
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
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.BcdType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

/**
 * Codec for packed BCD integer type.
 * <p>
 * It works with {@link BcdType} and maps the packed BCD value to {@code int}/{@link Integer}.
 *
 * @author Deng Ran
 * @since 3.13.0
 */
public class BcdCodec implements Codec<Integer> {
    @Override
    public Integer decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(BcdType.class);
            val order = context.getByteOrder(type::byteOrder);

            byte[] bytes = inputStream.readBytes(type.offset(), type.length());

            if (order == ByteOrder.LITTLE) {
                reverse(bytes);
            }

            int value = 0;

            for (byte b : bytes) {
                int hi = (b >> 4) & 0x0F;
                int lo = b & 0x0F;

                if (hi > 9 || lo > 9) {
                    throw new DecodingException("Invalid BCD digit.");
                }

                // Check for overflow before multiplying / adding.
                if (value > (Integer.MAX_VALUE - lo) / 100) {
                    throw new DecodingException("BCD value overflow int range.");
                }

                value = value * 10 + hi;
                value = value * 10 + lo;
            }

            return value;
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding BCD type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, Integer value) {
        try {
            if (value == null) {
                throw new EncodingException("Null value is not supported for BCD type.");
            }

            if (value < 0) {
                throw new EncodingException("Negative value is not supported for BCD type.");
            }

            val type = context.getDataTypeAnnotation(BcdType.class);
            val order = context.getByteOrder(type::byteOrder);

            int length = type.length();
            if (length <= 0) {
                throw new EncodingException("BCD length must be positive.");
            }

            byte[] bytes = new byte[length];
            int v = value;

            // Pack from least significant digits.
            for (int i = length - 1; i >= 0; i--) {
                int lo = v % 10;
                v /= 10;
                int hi = v % 10;
                v /= 10;

                if (hi > 9 || lo > 9) {
                    throw new EncodingException("Value cannot be represented as BCD with given length.");
                }

                bytes[i] = (byte) ((hi << 4) | lo);
            }

            // If v is not zero here, original value had more digits than we can represent.
            if (v != 0) {
                throw new EncodingException("Value too large for BCD length.");
            }

            if (order == ByteOrder.LITTLE) {
                reverse(bytes);
            }

            outputStream.writeBytes(type.offset(), bytes);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding BCD type.", e);
        }
    }

    protected static void reverse(byte[] bytes) {
        int i = 0, j = bytes.length - 1;
        while (i < j) {
            byte t = bytes[i];
            bytes[i] = bytes[j];
            bytes[j] = t;
            i++;
            j--;
        }
    }
}


