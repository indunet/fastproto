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
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.annotation.type.UInteger64Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.OutOfBoundsException;

import java.math.BigInteger;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class UInteger64Decoder implements TypeDecoder<BigInteger> {
    @Override
    public BigInteger decode(DecodeContext context) {
        val type = context.getTypeAnnotation(UInteger64Type.class);

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy());
    }

    public BigInteger decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy endian) {
        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (bo < 0) {
            throw new DecodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + UInteger64Type.SIZE > datagram.length) {
            throw new OutOfBoundsException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        long low = 0, high = 0;

        if (endian == EndianPolicy.LITTLE) {
            low |= (datagram[bo] & 0xFF);
            low |= ((datagram[bo + 1] & 0xFFL) << 8);
            low |= ((datagram[bo + 2] & 0xFFL) << 16);
            low |= ((datagram[bo + 3] & 0xFFL) << 24);

            high |= (datagram[bo + 4] & 0xFFL);
            high |= ((datagram[bo + 5] & 0xFFL) << 8);
            high |= ((datagram[bo + 6] & 0xFFL) << 16);
            high |= ((datagram[bo + 7] & 0xFFL) << 24);
        } else if (endian == EndianPolicy.BIG) {
            low |= (datagram[bo + 7] & 0xFF);
            low |= ((datagram[bo + 6] & 0xFFL) << 8);
            low |= ((datagram[bo + 5] & 0xFFL) << 16);
            low |= ((datagram[bo + 4] & 0xFFL) << 24);

            high |= (datagram[bo + 3] & 0xFFL);
            high |= ((datagram[bo + 2] & 0xFFL) << 8);
            high |= ((datagram[bo + 1] & 0xFFL) << 16);
            high |= ((datagram[bo] & 0xFFL) << 24);
        }

        return new BigInteger(String.valueOf(high))
                .multiply(new BigInteger(String.valueOf(UInteger32Type.MAX_VALUE + 1)))
                .add(new BigInteger(String.valueOf(low)));
    }
}
