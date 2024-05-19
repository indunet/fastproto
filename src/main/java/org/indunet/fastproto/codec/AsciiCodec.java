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
import org.indunet.fastproto.annotation.AsciiType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

/**
 * Codec for ASCII type.
 * This codec handles the encoding and decoding of ASCII characters.
 * It is used in conjunction with the AsciiType annotation.
 * The codec ensures that the ASCII value does not exceed the maximum value for a byte.
 * If the value exceeds the maximum, an exception is thrown.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class AsciiCodec implements Codec<Character> {
    @Override
    public Character decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(AsciiType.class);
            val value = inputStream.readUInt8(type.offset());

            if (value > Byte.MAX_VALUE) {
                throw new DecodingException(
                        String.format("%d is not valid ascii.", value));
            } else {
                return (char) value;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding ascii type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, Character value) {
        try {
            val type = context.getDataTypeAnnotation(AsciiType.class);

            if (value > Byte.MAX_VALUE) {
                throw new EncodingException(String.format("%c is not valid ascii.", value));
            } else {
                outputStream.writeUInt8(type.offset(), value);
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding ascii type.", e);
        }
    }
}
