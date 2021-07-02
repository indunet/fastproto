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

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.annotation.ProtocolVersion;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.annotation.type.UInteger16Type;
import org.indunet.fastproto.annotation.type.UInteger8Type;
import org.indunet.fastproto.decoder.DecodeUtils;
import org.indunet.fastproto.encoder.EncodeUtils;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.ProtocolVersionException;

/**
 * Protocol version assist.
 *
 * @author Deng Ran
 * @since 1.5.3
 */
public class ProtocolVersionAssist {
    public static boolean validate(@NonNull byte[] datagram, @NonNull TypeAssist assist) {
        if (!assist.getOpProtocolVersion().isPresent()) {
            return true;
        }

        ProtocolVersion protocolVersion = assist
                .getOpProtocolVersion()
                .get();

        return protocolVersion.version() == decode(datagram, assist);
    }

    public static int decode(@NonNull byte[] datagram, @NonNull TypeAssist assist) {
        if (!assist.getOpProtocolVersion().isPresent()) {
            return -1;
        }

        ProtocolVersion protocolVersion = assist
                .getOpProtocolVersion()
                .get();
        EndianPolicy policy = endianPolicy(assist);
        int byteOffset = protocolVersion.value();

        switch (protocolVersion.protocolType()) {
            case UINTEGER8:
                return DecodeUtils.uInteger8Type(datagram, byteOffset);
            case UINTEGER16:
                return DecodeUtils.uInteger16Type(datagram, byteOffset, policy);
            case INTEGER:
                return DecodeUtils.integerType(datagram, byteOffset, policy);
            default:
                throw new ProtocolVersionException(CodecError.ILLEGAL_PROTOCOL_VERSION_TYPE);
        }
    }

    public static void encode(@NonNull byte[] datagram, @NonNull TypeAssist assist) {
        if (!assist.getOpProtocolVersion().isPresent()) {
            return;
        }

        ProtocolVersion versionAnnotation = assist
                .getOpProtocolVersion()
                .get();
        EndianPolicy policy = endianPolicy(assist);
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
                throw new ProtocolVersionException(CodecError.ILLEGAL_PROTOCOL_VERSION_TYPE);
        }
    }

    public static EndianPolicy endianPolicy(@NonNull TypeAssist assist) {
        ProtocolVersion protocolVersion = assist
                .getOpProtocolVersion()
                .get();

        if (protocolVersion.endianPolicy().length > 0) {
            return protocolVersion.endianPolicy()[0];
        } else {
            return assist.getEndianPolicy();
        }
    }

    public static int size(@NonNull TypeAssist assist) {
        if (!assist.getOpProtocolVersion().isPresent()) {
            return 0;
        }

        val protocolVersion = assist
                .opProtocolVersion
                .get();

        switch (protocolVersion.protocolType()) {
            case UINTEGER8:
                return UInteger8Type.SIZE;
            case UINTEGER16:
                return UInteger16Type.SIZE;
            case INTEGER:
                return IntegerType.SIZE;
            default:
                throw new ProtocolVersionException(CodecError.ILLEGAL_PROTOCOL_VERSION_TYPE);
        }
    }
}