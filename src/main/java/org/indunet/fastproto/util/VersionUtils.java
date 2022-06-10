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
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.EnableVersion;
import org.indunet.fastproto.annotation.type.Int32Type;
import org.indunet.fastproto.annotation.type.UInt16Type;
import org.indunet.fastproto.annotation.type.UInt8Type;
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
        if (reference.getEnableVersion() == null) {
            return true;
        }

        EnableVersion enableVersion = reference.getEnableVersion();

        return enableVersion.version() == decode(datagram, reference);
    }

    public static int decode(@NonNull byte[] datagram, @NonNull Reference reference) {
        if (reference.getEnableVersion() == null) {
            return -1;
        }

        EnableVersion enableVersion = reference.getEnableVersion();
        EndianPolicy policy = endianPolicy(reference);
        int byteOffset = enableVersion.value();
        val type = enableVersion.genericType();

        if (type == ProtocolType.UINT8) {
            return CodecUtils.uinteger8Type(datagram, byteOffset);
        } else if (type == ProtocolType.UINT16) {
            return CodecUtils.uinteger16Type(datagram, byteOffset, policy);
        } else if (type == ProtocolType.INT32) {
            return CodecUtils.integerType(datagram, byteOffset, policy);
        } else {
            throw new ProtocolVersionException(CodecError.ILLEGAL_PROTOCOL_VERSION_TYPE);
        }
    }

    public static void encode(@NonNull byte[] datagram, @NonNull Reference reference) {
        if (reference.getEnableVersion() == null) {
            return;
        }

        EnableVersion enableVersion = reference.getEnableVersion();
        EndianPolicy policy = endianPolicy(reference);
        int byteOffset = enableVersion.value();
        int version = enableVersion.version();
        val type = enableVersion.genericType();

        if (type == ProtocolType.UINT8) {
            CodecUtils.uinteger8Type(datagram, byteOffset, version);
        } else if (type == ProtocolType.UINT16) {
            CodecUtils.uinteger16Type(datagram, byteOffset, policy, version);
        } else if (type == ProtocolType.INT32) {
            CodecUtils.integerType(datagram, byteOffset, policy, version);
        } else {
            throw new ProtocolVersionException(CodecError.ILLEGAL_PROTOCOL_VERSION_TYPE);
        }
    }

    public static EndianPolicy endianPolicy(@NonNull Reference reference) {
        EnableVersion enableProtocolVersion = reference.getEnableVersion();

        if (enableProtocolVersion.endianPolicy().length > 0) {
            return enableProtocolVersion.endianPolicy()[0];
        } else {
            return reference.getEndianPolicy();
        }
    }

    public static int size(@NonNull Reference reference) {
        if (reference.getEnableVersion() == null) {
            return 0;
        }

        val type = reference.getEnableVersion().genericType();

        if (type == ProtocolType.UINT8) {
            return UInt8Type.SIZE;
        } else if (type == ProtocolType.UINT16) {
            return UInt16Type.SIZE;
        } else if (type == ProtocolType.INT32) {
            return Int32Type.SIZE;
        } else {
            throw new ProtocolVersionException(CodecError.ILLEGAL_PROTOCOL_VERSION_TYPE);
        }
    }
}