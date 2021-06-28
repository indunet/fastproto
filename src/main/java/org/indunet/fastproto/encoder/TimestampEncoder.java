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

package org.indunet.fastproto.encoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.annotation.type.TimestampType;
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * Timestamp type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder
 * @since 1.1.0
 */
public class TimestampEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        EndianPolicy policy = context.getEndianPolicy();
        TimestampType type = context.getTypeAnnotation(TimestampType.class);
        Timestamp value = context.getValue(Timestamp.class);

        this.encode(context.getDatagram(), type.value(), type.protocolType(), policy, type.unit(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, @NonNull ProtocolType dataType, @NonNull EndianPolicy policy, @NonNull TimeUnit unit, @NonNull Timestamp value) {
        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (dataType == ProtocolType.LONG && unit == TimeUnit.MILLISECONDS) {
            if (bo + LongType.SIZE > datagram.length) {
                throw new SpaceNotEnoughException(CodecError.EXCEEDED_DATAGRAM_SIZE);
            }

            EncodeUtils.longType(datagram, bo, policy, value.getTime());
        } else if (dataType == ProtocolType.UINTEGER32 && unit == TimeUnit.SECONDS) {
            if (bo + UInteger32Type.SIZE > datagram.length) {
                throw new SpaceNotEnoughException(CodecError.EXCEEDED_DATAGRAM_SIZE);
            }

            EncodeUtils.integerType(datagram, bo, policy, (int) (value.getTime() / 1000));
        } else {
            throw new EncodeException(CodecError.ILLEGAL_TIMESTAMP_PARAMETERS);
        }
    }
}
