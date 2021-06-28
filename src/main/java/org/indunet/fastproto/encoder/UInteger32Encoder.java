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
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.IllegalValueException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;

import java.text.MessageFormat;

/**
 * UInteger32 type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,UInteger32Type
 * @since 1.2.0
 */
public class UInteger32Encoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        UInteger32Type type = context.getTypeAnnotation(UInteger32Type.class);
        EndianPolicy policy = context.getEndianPolicy();
        Long value = context.getValue(Long.class);

        this.encode(context.getDatagram(), type.value(), policy, value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, @NonNull EndianPolicy policy, long value) {
        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + UInteger32Type.SIZE > datagram.length) {
            throw new SpaceNotEnoughException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        } else if (value > UInteger32Type.MAX_VALUE || value < UInteger32Type.MIN_VALUE) {
            throw new IllegalValueException(
                    MessageFormat.format(CodecError.EXCEEDED_TYPE_SIZE_LIMIT.getMessage(), UInteger32Type.class.getName()));
        }

        if (policy == EndianPolicy.BIG) {
            datagram[bo + 3] = (byte) (value & 0xFF);
            datagram[bo + 2] = (byte) (value >> 8 & 0xFF);
            datagram[bo + 1] = (byte) (value >> 16 & 0xFF);
            datagram[bo] = (byte) (value >> 24 & 0xFF);
        } else {
            datagram[bo] = (byte) (value & 0xFF);
            datagram[bo + 1] = (byte) (value >> 8 & 0xFF);
            datagram[bo + 2] = (byte) (value >> 16 & 0xFF);
            datagram[bo + 3] = (byte) (value >> 24 & 0xFF);
        }
    }
}
