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
import org.indunet.fastproto.annotation.StringType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Codec for String type.
 * This codec is responsible for encoding and decoding String types.
 * It is used in conjunction with the StringType annotation.
 * If there are any issues during the encoding or decoding process, an exception is thrown.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class StringCodec implements Codec<String> {
    private static final StringCodec INSTANCE = new StringCodec();

    @Override
    public String decode(CodecContext context, ByteBufferInputStream inputStream) {

        try {
            val type = context.getDataTypeAnnotation(StringType.class);
            val set = Charset.forName(type.charset());
            val bytes = inputStream.readBytes(type.offset(), type.length());

            int end = bytes.length;
            while (end > 0 && bytes[end - 1] == 0) {
                end--;
            }
            return new String(bytes, 0, end, set);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding string type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, String value) {
        try {
            val type = context.getDataTypeAnnotation(StringType.class);
            val set = Charset.forName(type.charset());
            val src = value.getBytes(set);
            val l = outputStream.toByteBuffer().reverse(type.offset(), type.length());

            if (src.length >= l) {
                outputStream.writeBytes(type.offset(), Arrays.copyOfRange(src, 0, l));
            } else {
                byte[] padded = new byte[l]; // zero-filled by default
                System.arraycopy(src, 0, padded, 0, src.length);
                outputStream.writeBytes(type.offset(), padded);
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding string type.", e);
        }
    }

    public static class StringBufferCodec implements Codec<StringBuffer> {
        @Override
        public StringBuffer decode(CodecContext context, ByteBufferInputStream inputStream) {
            val str = INSTANCE.decode(context, inputStream);

            return new StringBuffer(str);
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, StringBuffer value) {
            INSTANCE.encode(context, outputStream, value.toString());
        }
    }

    public static class StringBuilderCodec implements Codec<StringBuilder> {
        @Override
        public StringBuilder decode(CodecContext context, ByteBufferInputStream inputStream) {
            val str = INSTANCE.decode(context, inputStream);

            return new StringBuilder(str);
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, StringBuilder value) {
            INSTANCE.encode(context, outputStream, value.toString());
        }
    }
}
