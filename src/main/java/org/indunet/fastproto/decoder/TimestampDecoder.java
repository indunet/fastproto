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
import org.indunet.fastproto.annotation.type.Int64Type;
import org.indunet.fastproto.annotation.type.TimeType;
import org.indunet.fastproto.annotation.type.UInt32Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.ReverseUtils;

import java.lang.annotation.Annotation;
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
        TimeType type = context.getTypeAnnotation(TimeType.class);
        val clazz = context.getReference()
                .getField().getType();

        return this.decode(context.getDatagram(), type.value(), type.genericType(), policy, type.unit(), (Class<T>) clazz);
    }

    public T decode(@NonNull final byte[] datagram, int byteOffset, @NonNull Class<? extends Annotation> genericType,
                    @NonNull EndianPolicy policy, @NonNull TimeUnit unit, Class<T> fieldType) {
        Function<Long, T> newInstance = v -> {
            if (fieldType == Timestamp.class) {
                return (T) new Timestamp(v);
            } else {
                return (T) new Date(v);
            }
        };

        int bo = ReverseUtils.offset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new DecodingException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (genericType == ProtocolType.LONG && unit == TimeUnit.MILLISECONDS) {
            if (bo + Int64Type.SIZE > datagram.length) {
                throw new DecodingException(CodecError.EXCEEDED_DATAGRAM_SIZE);
            }

            val value = CodecUtils.longType(datagram, bo, policy);

            return newInstance.apply(value);
        } else if (genericType == ProtocolType.UINT32 && unit == TimeUnit.SECONDS) {
            if (bo + UInt32Type.SIZE > datagram.length) {
                throw new DecodingException(CodecError.EXCEEDED_DATAGRAM_SIZE);
            }

            val value = CodecUtils.uinteger32Type(datagram, bo, policy);

            return newInstance.apply(value * 1000);
        } else {
            throw new DecodingException(CodecError.ILLEGAL_TIMESTAMP_PARAMETERS);
        }
    }
}
