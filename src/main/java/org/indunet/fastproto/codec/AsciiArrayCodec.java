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
import org.indunet.fastproto.annotation.Int8ArrayType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Ascii array type codec.
 *
 * @author Deng Ran
 * @since 3.9.1
 */
public class AsciiArrayCodec implements Codec<char[]> {
    public char[] decode(byte[] bytes, int offset, int length) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            val l = CodecUtils.reverse(bytes, offset, length);
            val chars = new char[l];

            IntStream.range(0, l)
                .forEach(i -> chars[i] = (char) CodecUtils.int8Type(bytes, o + i));

            return chars;
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding ascii array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, char[] values) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            val l = CodecUtils.reverse(bytes, offset, length);

            IntStream.range(0, l)
                    .forEach(i -> CodecUtils.int8Type(bytes, o + i, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding ascii array type.", e);
        }
    }

    @Override
    public char[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(Int8ArrayType.class);

        return this.decode(bytes, type.offset(), type.length());
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, char[] values) {
        val type = context.getDataTypeAnnotation(Int8ArrayType.class);

        try {
            val l = buffer.reverse(type.offset(), type.length());

            IntStream.range(0, l)
                    .forEach(i -> CodecUtils.int8Type(buffer, type.offset() + i, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding ascii array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Character[]> {
        @Override
        public Character[] decode(CodecContext context, byte[] bytes) {
            val chars = AsciiArrayCodec.this.decode(context, bytes);

            return IntStream.range(0, chars.length)
                    .mapToObj(i -> Character.valueOf(chars[i]))
                    .toArray(Character[]::new);
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Character[] values) {
            val chars = new char[values.length];

            IntStream.range(0, values.length)
                    .forEach(i -> chars[i] = values[i]);
            AsciiArrayCodec.this.encode(context, buffer, chars);
        }
    }

    public class CollectionCodec implements Codec<Collection<Character>> {
        @Override
        public Collection<Character> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Character> collection = CollectionUtils.newInstance(type);

                for (val c: AsciiArrayCodec.this.decode(context, bytes)) {
                    collection.add(c);
                }

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Collection<Character> collection) {
            val chars = new char[collection.size()];
            val values = collection.stream()
                    .toArray(Character[]::new);

            IntStream.range(0, chars.length)
                    .forEach(i -> chars[i] = values[i]);
            AsciiArrayCodec.this.encode(context, buffer, chars);
        }
    }
}
