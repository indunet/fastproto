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

import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.indunet.fastproto.annotation.type.EnumType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Enum type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class EnumCodec<T extends Enum> implements Codec<T> {
    public T decode(@NonNull final byte[] bytes, int offset, String fieldName, Class<T> enumClass) {
        val enums = enumClass.getEnumConstants();
        val code = CodecUtils.uint8Type(bytes, offset);

        if (fieldName.isEmpty()) {
            return Arrays.stream(enums)
                    .filter(e -> e.ordinal() == code)
                    .findAny()
                    .orElseThrow(() -> new DecodingException(MessageFormat.format(
                            CodecError.ENUM_NOT_FOUND.getMessage(), code)));
        } else {
            Function<Enum, Integer> getValue = (Enum enumObject) -> {
                Field field = null;
                try {
                    field = enumClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field.getInt(enumObject);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new DecodingException(MessageFormat.format(
                            CodecError.ILLEGAL_ENUM_CODE_FIELD.getMessage(), fieldName), e);
                }
            };

            return Arrays.stream(enums)
                    .filter(e -> getValue.apply(e) == code)
                    .findAny().
                    orElseThrow(() -> new DecodingException(MessageFormat.format(
                            CodecError.ENUM_NOT_FOUND.getMessage(), code)));
        }
    }

    public <T extends Enum> void encode(byte[] bytes, int offset, String fieldName, T value) {
        var code = 0;

        if (fieldName == null || fieldName.isEmpty()) {
            code = value.ordinal();
        } else {
            try {
                val field = value.getClass()
                        .getDeclaredField(fieldName);
                field.setAccessible(true);

                code = field.getInt(value);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new EncodingException(MessageFormat.format(
                        CodecError.ILLEGAL_ENUM_CODE_FIELD.getMessage(), fieldName), e);
            }
        }

        CodecUtils.uint8Type(bytes, offset, code);
    }

    @Override
    public T decode(CodecContext context, byte[] bytes) {
        val dataType = context.getDataType(EnumType.class);
        val fieldType = context.getFieldType();

        return this.decode(bytes, dataType.offset(), dataType.field(), (Class<T>) fieldType);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, T value) {
        val dataType = context.getDataType(EnumType.class);

        this.encode(bytes, dataType.offset(), dataType.field(), value);
    }
}
