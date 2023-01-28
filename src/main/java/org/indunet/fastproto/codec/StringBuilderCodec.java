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
import org.indunet.fastproto.annotation.StringType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.nio.charset.Charset;

/**
 * StringBuilder type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class StringBuilderCodec implements Codec<StringBuilder> {
    public StringBuilder decode(byte[] bytes, int offset, int length, Charset charset) {
        try {
            val binary = CodecUtils.binaryType(bytes, offset, length);
            val str = new String(binary, charset);

            return new StringBuilder(str);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding StingBuilder type.", e);
        }
    }

    public void encode(byte[] datagram, int offset, int length, Charset set, StringBuilder value) {
        try {
            CodecUtils.binaryType(datagram, offset, length, value.toString().getBytes());
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding StingBuilder type.", e);
        }
    }

    @Override
    public StringBuilder decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(StringType.class);

        return this.decode(bytes, type.offset(), type.length(), Charset.forName(type.charset()));
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, StringBuilder value) {
        val type = context.getDataTypeAnnotation(StringType.class);
        val charset = Charset.forName(type.charset());

        this.encode(bytes, type.offset(), type.length(), charset, value);
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, StringBuilder value) {
        val type = context.getDataTypeAnnotation(StringType.class);
        val charset = Charset.forName(type.charset());

        try {
            CodecUtils.binaryType(buffer, type.offset(), type.length(), value.toString().getBytes(charset));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding StingBuilder type.", e);
        }
    }
}
