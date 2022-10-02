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
import org.indunet.fastproto.annotation.type.Int16Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.util.Arrays;

/**
 * Short type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class ShortCodec implements Codec<Short> {
    public short decode(byte[] bytes, int offset, EndianPolicy policy) {
        try {
            return CodecUtils.shortType(bytes, offset, policy);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding int16(short) type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, EndianPolicy policy, short value) {
        try {
            CodecUtils.shortType(bytes, offset, policy, value);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding int16(short) type.", e);
        }
    }

    @Override
    public Short decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(Int16Type.class);
        val policy = Arrays.stream(type.endian())
                .findFirst()
                .orElseGet(context::getDefaultEndianPolicy);

        return this.decode(bytes, type.offset(), policy);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, Short value) {
        val type = context.getDataTypeAnnotation(Int16Type.class);
        val policy = Arrays.stream(type.endian())
                .findFirst()
                .orElseGet(context::getDefaultEndianPolicy);

        this.encode(bytes, type.offset(), policy, value);
    }
}
