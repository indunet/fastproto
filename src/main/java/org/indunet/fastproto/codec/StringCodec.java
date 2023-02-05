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
import org.indunet.fastproto.annotation.StringType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.CodecUtils;

import java.nio.charset.Charset;

/**
 * String type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class StringCodec implements Codec<String> {
    public String decode(byte[] bytes, int offset, int length, Charset charset) {
        try {
            val binary = CodecUtils.binaryType(bytes, offset, length);

            return new String(binary, charset);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding string type.", e);
        }
    }

    public void encode(byte[] datagram, int offset, int length, Charset set, String value) {
        try {
            CodecUtils.binaryType(datagram, offset, length, value.getBytes(set));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding string type.", e);
        }
    }

    @Override
    public String decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(StringType.class);
        val set = Charset.forName(type.charset());

        return this.decode(bytes, type.offset(), type.length(), set);
    }

    @Override
    public String decode(CodecContext context, ByteBufferInputStream inputStream) {

        try {
            val type = context.getDataTypeAnnotation(StringType.class);
            val set = Charset.forName(type.charset());
            val bytes = inputStream.readBytes(type.offset(), type.length());

            return new String(bytes, set);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding string type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, String value) {
        val type = context.getDataTypeAnnotation(StringType.class);
        val charset = Charset.forName(type.charset());

        try {
            CodecUtils.binaryType(buffer, type.offset(), type.length(), value.getBytes(charset));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding string type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, String value) {
        try {
            val type = context.getDataTypeAnnotation(StringType.class);
            val set = Charset.forName(type.charset());

            outputStream.writeBytes(type.offset(), type.length(), value.getBytes(set));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding string type.", e);
        }
    }

    public class StringBufferCodec implements Codec<StringBuffer> {
        @Override
        public StringBuffer decode(CodecContext context, byte[] bytes) {
            val str = StringCodec.this.decode(context, bytes);

            return new StringBuffer(str);
        }

        @Override
        public StringBuffer decode(CodecContext context, ByteBufferInputStream inputStream) {
            val str = StringCodec.this.decode(context, inputStream);

            return new StringBuffer(str);
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, StringBuffer value) {
            StringCodec.this.encode(context, buffer, value.toString());
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, StringBuffer value) {
            StringCodec.this.encode(context, outputStream, value.toString());
        }
    }

    public class StringBuilderCodec implements Codec<StringBuilder> {
        @Override
        public StringBuilder decode(CodecContext context, byte[] bytes) {
            val str = StringCodec.this.decode(context, bytes);

            return new StringBuilder(str);
        }

        @Override
        public StringBuilder decode(CodecContext context, ByteBufferInputStream inputStream) {
            val str = StringCodec.this.decode(context, inputStream);

            return new StringBuilder(str);
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, StringBuilder value) {
            StringCodec.this.encode(context, buffer, value.toString());
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, StringBuilder value) {
            StringCodec.this.encode(context, outputStream, value.toString());
        }
    }
}
