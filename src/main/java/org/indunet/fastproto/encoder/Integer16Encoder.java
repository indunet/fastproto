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
import org.indunet.fastproto.annotation.type.Integer16Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.IllegalValueException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;
import org.indunet.fastproto.util.ReverseUtils;

import java.text.MessageFormat;

/**
 * Integer16 type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,Integer16Type
 * @since 1.2.0
 */
public class Integer16Encoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        Integer16Type type = context.getTypeAnnotation(Integer16Type.class);
        EndianPolicy policy = context.getEndianPolicy();
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.value(), policy, value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, @NonNull EndianPolicy policy, int value) {
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + Integer16Type.SIZE > datagram.length) {
            throw new SpaceNotEnoughException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        } else if (value > Integer16Type.MAX_VALUE || value < Integer16Type.MIN_VALUE) {
            throw new IllegalValueException(
                    MessageFormat.format(CodecError.EXCEEDED_TYPE_SIZE_LIMIT.getMessage(), Integer16Type.class.getName()));
        }

        if (policy == EndianPolicy.BIG) {
            datagram[bo + 1] = (byte) (value);
            datagram[bo] = (byte) (value >>> 8);
        } else {
            datagram[bo] = (byte) (value);
            datagram[bo + 1] = (byte) (value >>> 8);
        }
    }
}
