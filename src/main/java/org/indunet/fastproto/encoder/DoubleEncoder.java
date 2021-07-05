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
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;
import org.indunet.fastproto.util.ReverseUtils;

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
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + DoubleType.SIZE > datagram.length) {
            throw new SpaceNotEnoughException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        long bits = Double.doubleToLongBits(value);

        if (endian == EndianPolicy.BIG) {
            datagram[bo + 7] = (byte) (bits & 0xFFL);
            datagram[bo + 6] = (byte) (bits >> 8 & 0xFFL);
            datagram[bo + 5] = (byte) (bits >> 16 & 0xFFL);
            datagram[bo + 4] = (byte) (bits >> 24 & 0xFFL);
            datagram[bo + 3] = (byte) (bits >> 32 & 0xFFL);
            datagram[bo + 2] = (byte) (bits >> 40 & 0xFFL);
            datagram[bo + 1] = (byte) (bits >> 48 & 0xFFL);
            datagram[bo] = (byte) (bits >> 56 & 0xFFL);
        } else {
            datagram[bo] = (byte) (bits & 0xFFL);
            datagram[bo + 1] = (byte) (bits >> 8 & 0xFFL);
            datagram[bo + 2] = (byte) (bits >> 16 & 0xFFL);
            datagram[bo + 3] = (byte) (bits >> 24 & 0xFFL);
            datagram[bo + 4] = (byte) (bits >> 32 & 0xFFL);
            datagram[bo + 5] = (byte) (bits >> 40 & 0xFFL);
            datagram[bo + 6] = (byte) (bits >> 48 & 0xFFL);
            datagram[bo + 7] = (byte) (bits >> 56 & 0xFFL);
        }
    }
}
