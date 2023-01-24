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
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Int64Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * Int64 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class Int64Codec implements Codec<Long> {
    public long decode(byte[] bytes, int byteOffset, ByteOrder endian) {
        try {
            return CodecUtils.int64Type(bytes, byteOffset, endian);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding int64 type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, ByteOrder policy, long value) {
        try {
            CodecUtils.int64Type(bytes, offset, policy, value);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding int64 type.", e);
        }
    }

    @Override
    public Long decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(Int64Type.class);
        val order = context.getByteOrder(type::byteOrder);

        return this.decode(bytes, type.offset(), order);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, Long value) {
        val type = context.getDataTypeAnnotation(Int64Type.class);
        val order = context.getByteOrder(type::byteOrder);

        this.encode(bytes, type.offset(), order, value);
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, Long value) {
        val type = context.getDataTypeAnnotation(Int64Type.class);
        val order = context.getByteOrder(type::byteOrder);

        try {
            CodecUtils.int64Type(buffer, type.offset(), order, value);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding int64 type.", e);
        }
    }
}
