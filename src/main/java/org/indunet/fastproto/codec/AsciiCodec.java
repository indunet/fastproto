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
import org.indunet.fastproto.ByteBuffer;
import org.indunet.fastproto.annotation.AsciiType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * ASCII type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class AsciiCodec implements Codec<Character> {
    public Character decode(final byte[] datagram, int offset) {
        try {
            val num = CodecUtils.uint8Type(datagram, offset);

            if (num > Byte.MAX_VALUE) {
                throw new DecodingException(
                        String.format("%d is not valid ascii.", num));
            } else {
                return (char) num;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding ascii type.", e);
        }
    }

    public void encode(byte[] datagram, int offset, char value) {
        try {
            if ((int) value > Byte.MAX_VALUE) {
                throw new EncodingException(String.format("%c is not valid ascii.", value));
            } else {
                CodecUtils.uint8Type(datagram, offset, value);
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding ascii type.", e);
        }
    }
    
    @Override
    public Character decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(AsciiType.class);
    
        return this.decode(bytes, type.offset());
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, Character value) {
        val type = context.getDataTypeAnnotation(AsciiType.class);

        try {
            int num = value;

            if (num > Byte.MAX_VALUE) {
                throw new EncodingException(String.format("%c is not valid ascii.", value));
            } else {
                CodecUtils.uint8Type(buffer, type.offset(), value);
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding ascii type.", e);
        }
    }
}
