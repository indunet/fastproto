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
import org.indunet.fastproto.annotation.type.UInt16Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * UInt16 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class UInt16Codec implements Codec<Integer> {
    public int decode(byte[] bytes, int offset, EndianPolicy policy) {
        try {
            return CodecUtils.uint16Type(bytes, offset, policy);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding uint16 type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, EndianPolicy policy, int value) {
        try {
            CodecUtils.uint16Type(bytes, offset, policy, value);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding uint16 type.", e);
        }
    }

    @Override
    public Integer decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(UInt16Type.class);
        val policy = context.getEndianPolicy();

        return this.decode(bytes, type.offset(), policy);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, Integer value) {
        val type = context.getDataTypeAnnotation(UInt16Type.class);
        val policy = context.getEndianPolicy();

        this.encode(bytes, type.offset(), policy, value);
    }
}
