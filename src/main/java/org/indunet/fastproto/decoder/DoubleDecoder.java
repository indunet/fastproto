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

package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.DoubleType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.OutOfBoundsException;
import org.indunet.fastproto.util.ReverseUtils;

/**
 * Double type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,DoubleType
 * @since 1.0.0
 */
public class DoubleDecoder implements TypeDecoder<Double> {
    @Override
    public Double decode(DecodeContext context) {
        DoubleType type = context.getTypeAnnotation(DoubleType.class);

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy());
    }

    public double decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy endian) {
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new DecodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + DoubleType.SIZE > datagram.length) {
            throw new OutOfBoundsException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        long value = 0;

        if (endian == EndianPolicy.LITTLE) {
            value |= (datagram[bo] & 0xFFL);
            value |= ((datagram[bo + 1] & 0xFFL) << 8);
            value |= ((datagram[bo + 2] & 0xFFL) << 16);
            value |= ((datagram[bo + 3] & 0xFFL) << 24);
            value |= ((datagram[bo + 4] & 0xFFL) << 32);
            value |= ((datagram[bo + 5] & 0xFFL) << 40);
            value |= ((datagram[bo + 6] & 0xFFL) << 48);
            value |= ((datagram[bo + 7] & 0xFFL) << 56);
        } else if (endian == EndianPolicy.BIG) {
            value |= (datagram[bo + 7] & 0xFFL);
            value |= ((datagram[bo + 6] & 0xFFL) << 8);
            value |= ((datagram[bo + 5] & 0xFFL) << 16);
            value |= ((datagram[bo + 4] & 0xFFL) << 24);
            value |= ((datagram[bo + 3] & 0xFFL) << 32);
            value |= ((datagram[bo + 2] & 0xFFL) << 40);
            value |= ((datagram[bo + 1] & 0xFFL) << 48);
            value |= ((datagram[bo] & 0xFFL) << 56);
        }

        return Double.longBitsToDouble(value);
    }
}
