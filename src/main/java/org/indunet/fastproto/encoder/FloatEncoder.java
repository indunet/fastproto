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
import org.indunet.fastproto.annotation.type.FloatType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * Float type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,FloatType
 * @since 1.0.0
 */
public class FloatEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        FloatType type = context.getTypeAnnotation(FloatType.class);
        Float value = context.getValue(Float.class);

        this.encode(context.getDatagram(), type.value(), context.getEndianPolicy(), value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy endian, float value) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + FloatType.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        int bits = Float.floatToIntBits(value);

        if (endian == EndianPolicy.LITTLE) {
            datagram[byteOffset] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 24 & 0xFFL);
        } else if (endian == EndianPolicy.BIG) {
            datagram[byteOffset + 3] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset] = (byte) (bits >> 24 & 0xFFL);
        }
    }
}
