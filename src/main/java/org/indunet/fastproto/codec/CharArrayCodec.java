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
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.CharType;
import org.indunet.fastproto.annotation.UInt16ArrayType;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * UInt16 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class CharArrayCodec implements Codec<char[]> {
    public char[] decode(byte[] bytes, int offset, int length, ByteOrder byteOrder) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * CharType.SIZE)  / CharType.SIZE + 1;
            }

            val chars = new char[l];

            IntStream.range(0, l)
                    .forEach(i -> chars[i] = (char) CodecUtils.uint16Type(bytes, o + i * CharType.SIZE, byteOrder));

            return chars;
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding char array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, ByteOrder policy, char[] values) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * UInt16Type.SIZE)  / UInt16Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i -> CodecUtils.uint16Type(bytes, o + i * UInt16Type.SIZE, policy, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding char array type.", e);
        }
    }

    @Override
    public char[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(UInt16ArrayType.class);
        val byteOrder = Arrays.stream(type.byteOrder())
                .findFirst()
                .orElseGet(context::getDefaultByteOrder);

        return this.decode(bytes, type.offset(), type.length(), byteOrder);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, char[] value) {
        val type = context.getDataTypeAnnotation(UInt16ArrayType.class);
        val byteOrder = Arrays.stream(type.byteOrder())
                .findFirst()
                .orElseGet(context::getDefaultByteOrder);

        this.encode(bytes, type.offset(), type.length(), byteOrder, value);
    }

    public class WrapperCodec implements Codec<Character[]> {
        @Override
        public Character[] decode(CodecContext context, byte[] bytes) {
            val chars = CharArrayCodec.this.decode(context, bytes);

            return IntStream.range(0, chars.length)
                    .mapToObj(i -> Character.valueOf(chars[i]))
                    .toArray(Character[]::new);
        }

        @Override
        public void encode(CodecContext context, byte[] bytes, Character[] values) {
            val chars = new char[values.length];

            IntStream.range(0, values.length)
                    .forEach(i -> chars[i] = values[i]);
            CharArrayCodec.this.encode(context, bytes, chars);
        }
    }

    public class CollectionCodec implements Codec<Collection<Character>> {
        @Override
        public Collection<Character> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Character> collection = CollectionUtils.newInstance(type);

                for (val c: CharArrayCodec.this.decode(context, bytes)) {
                    collection.add(c);
                }

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, byte[] bytes, Collection<Character> collection) {
            val chars = new char[collection.size()];
            val values = collection.stream()
                    .toArray(Character[]::new);

            IntStream.range(0, chars.length)
                    .forEach(i -> chars[i] = values[i]);
            CharArrayCodec.this.encode(context, bytes, chars);
        }
    }
}
