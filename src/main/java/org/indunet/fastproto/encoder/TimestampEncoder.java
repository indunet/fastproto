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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.Int64Type;
import org.indunet.fastproto.annotation.type.TimeType;
import org.indunet.fastproto.annotation.type.UInt32Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.ReverseUtils;

import java.lang.annotation.Annotation;
import java.util.Date;
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
        TimeType type = context.getTypeAnnotation(TimeType.class);
        val value = context.getValue(Date.class);

        this.encode(context.getDatagram(), type.offset(), type.genericType(), policy, type.unit(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, @NonNull Class<? extends Annotation> type, @NonNull EndianPolicy policy, @NonNull TimeUnit unit, @NonNull Date value) {
        int bo = ReverseUtils.offset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new EncodingException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (type == ProtocolType.INT64 && unit == TimeUnit.MILLISECONDS) {
            if (bo + Int64Type.SIZE > datagram.length) {
                throw new EncodingException(CodecError.EXCEEDED_DATAGRAM_SIZE);
            }

            CodecUtils.int64Type(datagram, bo, policy, value.getTime());
        } else if (type == ProtocolType.UINT32 && unit == TimeUnit.SECONDS) {
            if (bo + UInt32Type.SIZE > datagram.length) {
                throw new EncodingException(CodecError.EXCEEDED_DATAGRAM_SIZE);
            }

            CodecUtils.int32Type(datagram, bo, policy, (int) (value.getTime() / 1000));
        } else {
            throw new EncodingException(CodecError.ILLEGAL_TIMESTAMP_PARAMETERS);
        }
    }
}
