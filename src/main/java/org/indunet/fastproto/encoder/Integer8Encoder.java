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

import org.indunet.fastproto.annotation.type.Integer8Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.IllegalValueException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;

import java.text.MessageFormat;

/**
 * Integer8 type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,Integer8Type
 * @since 1.2.0
 */
public class Integer8Encoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        Integer8Type type = context.getTypeAnnotation(Integer8Type.class);
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.value(), value);
    }

    public void encode(byte[] datagram, int byteOffset, int value) {
        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + Integer8Type.SIZE > datagram.length) {
            throw new SpaceNotEnoughException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        } else if (value > Integer8Type.MAX_VALUE || value < Integer8Type.MIN_VALUE) {
            throw new IllegalValueException(
                    MessageFormat.format(CodecError.EXCEEDED_TYPE_SIZE_LIMIT.getMessage(), Integer8Type.class.getName()));
        }

        datagram[bo] = (byte) value;
    }
}
