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
import org.indunet.fastproto.annotation.EnumType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Enum type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class EnumCodec<T extends Enum> implements Codec<T> {
    @Override
    public T decode(CodecContext context, ByteBufferInputStream inputStream) {
        val type = context.getDataTypeAnnotation(EnumType.class);
        val enumClass = (Class<T>) context.getFieldType();
        val enums = enumClass.getEnumConstants();
        val code = inputStream.readUInt8(type.offset());
        val name = type.name();

        if (name == null || name.isEmpty()) {
            return Arrays.stream(enums)
                    .filter(e -> e.ordinal() == code)
                    .findAny()
                    .orElseThrow(() -> new DecodingException(String.format("Enum with code %s cannot be found.", code)));
        } else {
            Function<Enum, Integer> getValue = (Enum enumObject) -> {
                Field field = null;

                try {
                    field = enumClass.getDeclaredField(name);
                    field.setAccessible(true);
                    return field.getInt(enumObject);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new DecodingException(String.format("Illegal enum field %s", name), e);
                }
            };

            return Arrays.stream(enums)
                    .filter(e -> getValue.apply(e) == code)
                    .findAny().
                    orElseThrow(() -> new DecodingException(String.format("Enum with code %s cannot be found.", code)));
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, T value) {
        val type = context.getDataTypeAnnotation(EnumType.class);
        val name = type.name();
        var code = 0;

        if (name == null || name.isEmpty()) {
            code = value.ordinal();
        } else {
            try {
                val field = value.getClass()
                        .getDeclaredField(name);

                field.setAccessible(true);
                code = field.getInt(value);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new EncodingException(String.format("Illegal enum field %s", name), e);
            }
        }

        outputStream.writeUInt8(type.offset(), code);
    }
}
