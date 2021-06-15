/*
 * Copyright 2019-2021 indunet
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

package org.indunet.fastproto;

import lombok.val;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.ProtocolVersion;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;
import org.indunet.fastproto.util.DecodeUtils;
import org.indunet.fastproto.util.EncodeUtils;

import java.util.Optional;

/**
 * Protocol version assist.
 *
 * @author Deng Ran
 * @since 1.5.3
 */
public class VersionAssist {
    public static boolean validate(byte[] datagram, Class<?> protocolClass) {
        if (!protocolClass.isAnnotationPresent(ProtocolVersion.class)) {
            return true;
        }

        ProtocolVersion versionAnnotation = protocolClass.getAnnotation(ProtocolVersion.class);

        val i = decode(datagram, protocolClass);
        return versionAnnotation.version() == i;
    }

    public static int decode(byte[] datagram, Class<?> protocolClass) {
        if (!protocolClass.isAnnotationPresent(ProtocolVersion.class)) {
            return -1;
        }

        ProtocolVersion versionAnnotation = protocolClass.getAnnotation(ProtocolVersion.class);
        EndianPolicy policy = Optional.ofNullable(protocolClass.getAnnotation(Endian.class))
                .map(Endian::value)
                .orElse(EndianPolicy.LITTLE);
        int byteOffset = versionAnnotation.value();

        switch (versionAnnotation.protocolType()) {
            case UINTEGER8:
                return DecodeUtils.uInteger8Type(datagram, byteOffset);
            case UINTEGER16:
                return DecodeUtils.uInteger16Type(datagram, byteOffset, policy);
            case INTEGER:
                return DecodeUtils.integerType(datagram, byteOffset, policy);
            default:
                throw new DecodeException(DecodeError.ILLEGAL_PROTOCOL_VERSION_TYPE);
        }
    }

    public static void encode(byte[] datagram, Class<?> protocolClass) {
        if (protocolClass.isAnnotationPresent(ProtocolVersion.class) == false) {
            return;
        }

        ProtocolVersion versionAnnotation = protocolClass.getAnnotation(ProtocolVersion.class);
        EndianPolicy policy = Optional.ofNullable(protocolClass.getAnnotation(Endian.class))
                .map(Endian::value)
                .orElse(EndianPolicy.LITTLE);
        int byteOffset = versionAnnotation.value();
        int version = versionAnnotation.version();

        switch (versionAnnotation.protocolType()) {
            case UINTEGER8:
                EncodeUtils.uInteger8Type(datagram, byteOffset, version);
                break;
            case UINTEGER16:
                EncodeUtils.uInteger16Type(datagram, byteOffset, policy, version);
                break;
            case INTEGER:
                EncodeUtils.integerType(datagram, byteOffset, policy, version);
                break;
            default:
                throw new EncodeException(EncodeError.ILLEGAL_PROTOCOL_VERSION_TYPE);
        }
    }
}