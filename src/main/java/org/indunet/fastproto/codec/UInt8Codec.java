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
import org.indunet.fastproto.io.ByteBuffer;
import org.indunet.fastproto.annotation.UInt8Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * UInt8 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class UInt8Codec implements Codec<Integer> {
    @Override
    public Integer decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(UInt8Type.class);

        try {
            return CodecUtils.uint8Type(bytes, type.offset());
        } catch(ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding uint8 type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, Integer value) {
        val type = context.getDataTypeAnnotation(UInt8Type.class);

        try {
            CodecUtils.uint8Type(buffer, type.offset(), value);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding uint8 type.", e);
        }
    }
}
