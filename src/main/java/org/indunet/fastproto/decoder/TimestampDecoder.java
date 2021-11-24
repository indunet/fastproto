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
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.annotation.type.TimestampType;
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.ReverseUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


/**
 * Timestamp type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.1.0
 */
public class TimestampDecoder<T extends Date> implements TypeDecoder<T> {
    @Override
    public T decode(@NonNull DecodeContext context) {
        EndianPolicy policy = context.getEndianPolicy();
        TimestampType type = context.getTypeAnnotation(TimestampType.class);
        ProtocolType dataType = type.genericType();
        val clazz = context.getReference()
                .getField().getType();

        return this.decode(context.getDatagram(), type.value(), dataType, policy, type.unit(), (Class<T>) clazz);
    }

    public T decode(@NonNull final byte[] datagram, int byteOffset, @NonNull ProtocolType dataType, @NonNull EndianPolicy policy, @NonNull TimeUnit unit, Class<T> clazz) {
        Function<Long, T> newInstance = v -> {
            if (clazz == Timestamp.class) {
                return (T) new Timestamp(v);
            } else {
                return (T) new Date(v);
            }
        };

        int bo = ReverseUtils.offset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new DecodingException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (dataType == ProtocolType.LONG && unit == TimeUnit.MILLISECONDS) {
            if (bo + LongType.SIZE > datagram.length) {
                throw new DecodingException(CodecError.EXCEEDED_DATAGRAM_SIZE);
            }

            val value = CodecUtils.longType(datagram, bo, policy);

            return newInstance.apply(value);
        } else if (dataType == ProtocolType.UINTEGER32 && unit == TimeUnit.SECONDS) {
            if (bo + UInteger32Type.SIZE > datagram.length) {
                throw new DecodingException(CodecError.EXCEEDED_DATAGRAM_SIZE);
            }

            val value = CodecUtils.uinteger32Type(datagram, bo, policy);

            return newInstance.apply(value * 1000);
        } else {
            throw new DecodingException(CodecError.ILLEGAL_TIMESTAMP_PARAMETERS);
        }
    }
}
