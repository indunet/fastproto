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
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.annotation.type.UInteger64Type;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.math.BigInteger;
import java.text.MessageFormat;

/**
 * @author Deng Ran
 * @since 1.5.0
 */
public class UInteger64Encoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        val type = context.getTypeAnnotation(UInteger64Type.class);
        val value = context.getValue(BigInteger.class);

        this.encode(context.getDatagram(), type.value(), context.getEndianPolicy(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, @NonNull EndianPolicy policy, BigInteger value) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + LongType.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        } else if (value.compareTo(UInteger64Type.MAX_VALUE) > 0 || value.compareTo(UInteger64Type.MIN_VALUE) < 0) {
            throw new EncodeException(
                    MessageFormat.format(EncodeError.EXCEEDED_TYPE_SIZE_LIMIT.getMessage(), UInteger64Type.class.getName()));
        }

        long low = value
                .and(new BigInteger(String.valueOf(0xFFFF_FFFFL)))
                .longValueExact();
        long high = value
                .shiftRight(32)
                .longValueExact();

        if (policy == EndianPolicy.BIG) {
            datagram[byteOffset + 7] = (byte) (low & 0xFFL);
            datagram[byteOffset + 6] = (byte) (low >> 8 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (low >> 16 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (low >> 24 & 0xFFL);

            datagram[byteOffset + 3] = (byte) (high & 0xFFL);
            datagram[byteOffset + 2] = (byte) (high >> 8 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (high >> 16 & 0xFFL);
            datagram[byteOffset] = (byte) (high >> 24 & 0xFFL);
        } else {
            datagram[byteOffset] = (byte) (low & 0xFFL);
            datagram[byteOffset + 1] = (byte) (low >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (low >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (low >> 24 & 0xFFL);

            datagram[byteOffset + 4] = (byte) (high & 0xFFL);
            datagram[byteOffset + 5] = (byte) (high >> 8 & 0xFFL);
            datagram[byteOffset + 6] = (byte) (high >> 16 & 0xFFL);
            datagram[byteOffset + 7] = (byte) (high >> 24 & 0xFFL);
        }
    }
}
