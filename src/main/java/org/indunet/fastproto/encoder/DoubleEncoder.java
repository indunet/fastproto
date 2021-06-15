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

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.DoubleType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * Double type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,DoubleType
 * @since 1.0.0
 */
public class DoubleEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        DoubleType type = context.getTypeAnnotation(DoubleType.class);
        Double value = context.getValue(Double.class);

        this.encode(context.getDatagram(), type.value(), context.getEndianPolicy(), value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy endian, double value) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + DoubleType.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        long bits = Double.doubleToLongBits(value);

        if (endian == EndianPolicy.BIG) {
            datagram[byteOffset + 7] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 6] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (bits >> 24 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 32 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 40 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 48 & 0xFFL);
            datagram[byteOffset] = (byte) (bits >> 56 & 0xFFL);
        } else {
            datagram[byteOffset] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 24 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (bits >> 32 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (bits >> 40 & 0xFFL);
            datagram[byteOffset + 6] = (byte) (bits >> 48 & 0xFFL);
            datagram[byteOffset + 7] = (byte) (bits >> 56 & 0xFFL);
        }
    }
}
