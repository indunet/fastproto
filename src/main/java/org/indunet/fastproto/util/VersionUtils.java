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

package org.indunet.fastproto.util;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.EnableVersion;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.annotation.type.UInteger16Type;
import org.indunet.fastproto.annotation.type.UInteger8Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.ProtocolVersionException;
import org.indunet.fastproto.graph.Reference;

/**
 * Protocol version utils.
 *
 * @author Deng Ran
 * @since 1.5.3
 */
public class VersionUtils {
    public static boolean validate(@NonNull byte[] datagram, @NonNull Reference reference) {
        if (reference.getEnableProtocolVersion() == null) {
            return true;
        }

        EnableVersion enableVersion = reference.getEnableProtocolVersion();

        return enableVersion.version() == decode(datagram, reference);
    }

    public static int decode(@NonNull byte[] datagram, @NonNull Reference reference) {
        if (reference.getEnableProtocolVersion() == null) {
            return -1;
        }

        EnableVersion enableProtocolVersion = reference.getEnableProtocolVersion();
        EndianPolicy policy = endianPolicy(reference);
        int byteOffset = enableProtocolVersion.value();

        switch (enableProtocolVersion.protocolType()) {
            case UINTEGER8:
                return CodecUtils.uinteger8Type(datagram, byteOffset);
            case UINTEGER16:
                return CodecUtils.uinteger16Type(datagram, byteOffset, policy);
            case INTEGER:
                return CodecUtils.integerType(datagram, byteOffset, policy);
            default:
                throw new ProtocolVersionException(CodecError.ILLEGAL_PROTOCOL_VERSION_TYPE);
        }
    }

    public static void encode(@NonNull byte[] datagram, @NonNull Reference reference) {
        if (reference.getEnableProtocolVersion() == null) {
            return;
        }

        EnableVersion versionAnnotation = reference.getEnableProtocolVersion();
        EndianPolicy policy = endianPolicy(reference);
        int byteOffset = versionAnnotation.value();
        int version = versionAnnotation.version();

        switch (versionAnnotation.protocolType()) {
            case UINTEGER8:
                CodecUtils.uinteger8Type(datagram, byteOffset, version);
                break;
            case UINTEGER16:
                CodecUtils.uinteger16Type(datagram, byteOffset, policy, version);
                break;
            case INTEGER:
                CodecUtils.integerType(datagram, byteOffset, policy, version);
                break;
            default:
                throw new ProtocolVersionException(CodecError.ILLEGAL_PROTOCOL_VERSION_TYPE);
        }
    }

    public static EndianPolicy endianPolicy(@NonNull Reference reference) {
        EnableVersion enableProtocolVersion = reference.getEnableProtocolVersion();

        if (enableProtocolVersion.endianPolicy().length > 0) {
            return enableProtocolVersion.endianPolicy()[0];
        } else {
            return reference.getEndianPolicy();
        }
    }

    public static int size(@NonNull Reference reference) {
        if (reference.getEnableProtocolVersion() == null) {
            return 0;
        }

        val enableProtocolVersion = reference.getEnableProtocolVersion();

        switch (enableProtocolVersion.protocolType()) {
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