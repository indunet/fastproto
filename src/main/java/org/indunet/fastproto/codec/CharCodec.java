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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.CharType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.util.Arrays;

/**
 * Char type codec.
 *
 * @author Deng Ran
 * @since 3.8.4
 */
public class CharCodec implements Codec<Character> {
    public Character decode(final byte[] datagram, int offset, EndianPolicy policy) {
        try {
            return (char) CodecUtils.uint16Type(datagram, offset, policy);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding char type.", e);
        }
    }

    public void encode(byte[] datagram, int offset, EndianPolicy policy, char value) {
        try {
            CodecUtils.uint16Type(datagram, offset, policy, value);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding char type.", e);
        }
    }

    @Override
    public Character decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(CharType.class);
        val policy = Arrays.stream(type.endian())
                .findFirst()
                .orElseGet(context::getDefaultEndianPolicy);

        return this.decode(bytes, type.offset(), policy);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, Character value) {
        val type = context.getDataTypeAnnotation(CharType.class);
        val policy = Arrays.stream(type.endian())
                .findFirst()
                .orElseGet(context::getDefaultEndianPolicy);

        this.encode(bytes, type.offset(), policy, value);
    }
}
