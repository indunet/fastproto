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
import org.indunet.fastproto.annotation.type.DateType;
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.encoder.TypeEncoder;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.DecodeUtils;
import org.indunet.fastproto.util.ReverseUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Date type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder
 * @since 2.2.0
 */
public class DateDecoder implements TypeDecoder<Date> {
    @Override
    public Date decode(@NonNull DecodeContext context) {
        val policy = context.getEndianPolicy();
        val type = context.getTypeAnnotation(DateType.class);
        val dataType = type.protocolType();

        return this.decode(context.getDatagram(), type.value(), dataType, policy, type.unit());
    }

    public Date decode(@NonNull final byte[] datagram, int byteOffset, @NonNull ProtocolType dataType, @NonNull EndianPolicy policy, @NonNull TimeUnit unit) {
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new DecodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (dataType == ProtocolType.LONG && unit == TimeUnit.MILLISECONDS) {
            if (bo + LongType.SIZE > datagram.length) {
                throw new DecodeException(CodecError.EXCEEDED_DATAGRAM_SIZE);
            }

            val value = DecodeUtils.longType(datagram, bo, policy);

            return new Date(value);
        } else if (dataType == ProtocolType.UINTEGER32 && unit == TimeUnit.SECONDS) {
            if (bo + UInteger32Type.SIZE > datagram.length) {
                throw new DecodeException(CodecError.EXCEEDED_DATAGRAM_SIZE);
            }

            val value = DecodeUtils.uInteger32Type(datagram, bo, policy);

            return new Date(value * 1000);
        } else {
            throw new DecodeException(CodecError.ILLEGAL_TIMESTAMP_PARAMETERS);
        }
    }
}
