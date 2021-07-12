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

package org.indunet.fastproto.encoder;

import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.EnumType;
import org.indunet.fastproto.decoder.TypeDecoder;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;

import java.text.MessageFormat;

/**
 * Enum type encoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 2.1.0
 */
public class EnumEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        val type = context.getTypeAnnotation(EnumType.class);
        val value  = context.getValue(Enum.class);

        this.encode(context.getDatagram(), type.value(), context.getEndianPolicy(),
                type.protocolType(), type.fieldName(), value);
    }

    public <T extends Enum> void encode(@NonNull byte[] datagram, int byteOffset, EndianPolicy policy,
                                        @NonNull ProtocolType protocolType, @NonNull String fieldName, @NonNull T value) {
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
                throw new EncodeException(MessageFormat.format(
                        CodecError.ILLEGAL_ENUM_CODE_FIELD.getMessage(), fieldName), e);
            }
        }

        if (protocolType == ProtocolType.UINTEGER8) {
            EncodeUtils.uInteger8Type(datagram, byteOffset, code);
        } else if (protocolType == ProtocolType.UINTEGER16) {
            EncodeUtils.uInteger16Type(datagram, byteOffset, policy, code);
        } else if (protocolType == ProtocolType.INTEGER) {
            EncodeUtils.integerType(datagram, byteOffset, policy, code);
        } else {
            throw new EncodeException(CodecError.INVALID_ENUM_PROTOCOL_TYPE);
        }
    }
}
