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
import lombok.var;
import org.indunet.fastproto.annotation.CharArrayType;
import org.indunet.fastproto.annotation.CharType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * UInt16 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class CharArrayCodec implements Codec<char[]> {
    @Override
    public char[] decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(CharArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = inputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = inputStream.toByteBuffer().reverse(type.offset(), type.length() * CharType.SIZE)  / CharType.SIZE + 1;
            }

            val chars = new char[l];

            IntStream.range(0, l)
                    .forEach(i -> chars[i] = (char) inputStream.readUInt16(o + i * CharType.SIZE, order));

            return chars;
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding char array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, char[] values) {
        try {
            val type = context.getDataTypeAnnotation(CharArrayType.class);
            val order = context.getByteOrder(type::byteOrder);
            val o = outputStream.toByteBuffer().reverse(type.offset());
            var l = type.length();

            if (l < 0) {
                l = outputStream.toByteBuffer().reverse(type.offset(), type.length() * CharType.SIZE)  / CharType.SIZE + 1;
            }

            IntStream.range(0, Math.min(l, values.length))
                    .forEach(i -> outputStream.writeUInt16(o + i * CharType.SIZE, order, values[i]));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding char array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Character[]> {
        @Override
        public Character[] decode(CodecContext context, ByteBufferInputStream inputStream) {
            val chars = CharArrayCodec.this.decode(context, inputStream);

            return IntStream.range(0, chars.length)
                    .mapToObj(i -> Character.valueOf(chars[i]))
                    .toArray(Character[]::new);
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Character[] values) {
            val chars = new char[values.length];

            IntStream.range(0, values.length)
                    .forEach(i -> chars[i] = values[i]);
            CharArrayCodec.this.encode(context, outputStream, chars);
        }
    }

    public class CollectionCodec implements Codec<Collection<Character>> {
        @Override
        public Collection<Character> decode(CodecContext context, ByteBufferInputStream inputStream) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Character> collection = CollectionUtils.newInstance(type);

                for (val c: CharArrayCodec.this.decode(context, inputStream)) {
                    collection.add(c);
                }

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Collection<Character> collection) {
            val chars = new char[collection.size()];
            val values = collection.stream()
                    .toArray(Character[]::new);

            IntStream.range(0, chars.length)
                    .forEach(i -> chars[i] = values[i]);
            CharArrayCodec.this.encode(context, outputStream, chars);
        }
    }
}
