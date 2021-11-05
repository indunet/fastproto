/*
 * Copyright 2019-2021 indunet.org
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

package org.indunet.fastproto.decoder;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.EnumType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.CodecUtils;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Enum type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 2.1.0
 */
public class EnumDecoder<T extends Enum> implements TypeDecoder<T> {
    @Override
    public T decode(DecodeContext context) {
        val type = context.getTypeAnnotation(EnumType.class);
        val enumClass = context
                .getTypeAssist()
                .getClazz();

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy(),
                type.protocolType(), type.fieldName(), (Class<T>) enumClass);
    }

    public T decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy policy,
                    @NonNull ProtocolType type, @NonNull String fieldName, @NonNull Class<T> enumClass) {
        val enums = enumClass.getEnumConstants();
        val code = getCode(datagram, byteOffset, policy, type);

        if (fieldName.isEmpty()) {
            return Arrays.stream(enums)
                    .filter(e -> e.ordinal() == code)
                    .findAny()
                    .orElseThrow(() -> new DecodeException(MessageFormat.format(
                            CodecError.ENUM_NOT_FOUND.getMessage(), code)));
        } else {
            Function<Enum, Integer> getValue = (Enum enumObject) -> {
                Field field = null;
                try {
                    field = enumClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field.getInt(enumObject);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new DecodeException(MessageFormat.format(
                            CodecError.ILLEGAL_ENUM_CODE_FIELD.getMessage(), fieldName), e);
                }
            };

            return Arrays.stream(enums)
                    .filter(e -> getValue.apply(e) == code)
                    .findAny().
                            orElseThrow(() -> new DecodeException(MessageFormat.format(
                                    CodecError.ENUM_NOT_FOUND.getMessage(), code)));
        }
    }

    public int getCode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy policy,
                       @NonNull ProtocolType type) {
        if (type == ProtocolType.UINTEGER8) {
            return CodecUtils.uinteger8Type(datagram, byteOffset);
        } else if (type == ProtocolType.UINTEGER16) {
            return CodecUtils.uinteger16Type(datagram, byteOffset, policy);
        } else if (type == ProtocolType.INTEGER) {
            return CodecUtils.integerType(datagram, byteOffset, policy);
        } else {
            throw new DecodeException(CodecError.INVALID_ENUM_PROTOCOL_TYPE);
        }
    }
}
