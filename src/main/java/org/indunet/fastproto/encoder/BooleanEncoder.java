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

import org.indunet.fastproto.annotation.type.BooleanType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;
import org.indunet.fastproto.util.ReverseUtils;

/**
 * Boolean type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,BooleanType
 * @since 1.0.0
 */
public class BooleanEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        BooleanType type = context.getTypeAnnotation(BooleanType.class);
        Boolean value = context.getValue(Boolean.class);

        this.encode(context.getDatagram(), type.value(), type.bitOffset(), value);
    }

    public void encode(byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        int bo = ReverseUtils.offset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo >= datagram.length) {
            throw new EncodeException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        } else if (bitOffset < BooleanType.MIN_BIT_OFFSET || bitOffset > BooleanType.MAX_BIT_OFFSET) {
            throw new SpaceNotEnoughException(CodecError.ILLEGAL_BIT_OFFSET);
        }

        if (value) {
            datagram[bo] |= (0x01 << bitOffset);
        } else {
            datagram[bo] &= ~(0x01 << bitOffset);
        }
    }
}
