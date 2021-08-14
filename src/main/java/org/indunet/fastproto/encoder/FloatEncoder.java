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
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.util.ReverseUtils;

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
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + FloatType.SIZE > datagram.length) {
            throw new EncodeException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        int bits = Float.floatToIntBits(value);

        if (endian == EndianPolicy.LITTLE) {
            datagram[bo] = (byte) bits;
            datagram[bo + 1] = (byte) (bits >>> 8);
            datagram[bo + 2] = (byte) (bits >>> 16);
            datagram[bo + 3] = (byte) (bits >>> 24);
        } else if (endian == EndianPolicy.BIG) {
            datagram[bo + 3] = (byte) (bits);
            datagram[bo + 2] = (byte) (bits >>> 8);
            datagram[bo + 1] = (byte) (bits >>> 16);
            datagram[bo] = (byte) (bits >>> 24);
        }
    }
}
