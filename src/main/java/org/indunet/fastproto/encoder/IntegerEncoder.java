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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;
import org.indunet.fastproto.util.ReverseUtils;

/**
 * Integer type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,IntegerType
 * @since 1.0.0
 */
public class IntegerEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        IntegerType type = context.getTypeAnnotation(IntegerType.class);
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.value(), context.getEndianPolicy(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, @NonNull EndianPolicy policy, int value) {
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + IntegerType.SIZE > datagram.length) {
            throw new SpaceNotEnoughException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (policy == EndianPolicy.LITTLE) {
            datagram[bo] = (byte) (value & 0xFF);
            datagram[bo + 1] = (byte) (value >> 8 & 0xFF);
            datagram[bo + 2] = (byte) (value >> 16 & 0xFF);
            datagram[bo + 3] = (byte) (value >> 24 & 0xFF);
        } else if (policy == EndianPolicy.BIG) {
            datagram[bo + 3] = (byte) (value & 0xFF);
            datagram[bo + 2] = (byte) (value >> 8 & 0xFF);
            datagram[bo + 1] = (byte) (value >> 16 & 0xFF);
            datagram[bo] = (byte) (value >> 24 & 0xFF);
        }
    }
}
