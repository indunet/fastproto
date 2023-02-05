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
import org.indunet.fastproto.annotation.Int16Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.CodecUtils;

/**
 * Short type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class ShortCodec implements Codec<Short> {
    @Override
    public Short decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(Int16Type.class);
        val order = context.getByteOrder(type::byteOrder);

        try {
            return CodecUtils.shortType(bytes, type.offset(), order);
        } catch (IndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding int16(short) type.", e);
        }
    }

    @Override
    public Short decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(Int16Type.class);
            val order = context.getByteOrder(type::byteOrder);

            return inputStream.readShort(type.offset(), order);
        } catch (IndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding int16(short) type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, Short value) {
        val type = context.getDataTypeAnnotation(Int16Type.class);
        val order = context.getByteOrder(type::byteOrder);

        try {
            CodecUtils.shortType(buffer, type.offset(), order, value);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding int16(short) type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, Short value) {
        try {
            val type = context.getDataTypeAnnotation(Int16Type.class);
            val order = context.getByteOrder(type::byteOrder);

            outputStream.writeShort(type.offset(), order, value);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding int16(short) type.", e);
        }
    }
}
