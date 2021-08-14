/*
 * Copyright 2019-2021 indunet.org
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
import org.indunet.fastproto.annotation.type.UInteger8Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.util.ReverseUtils;

import java.text.MessageFormat;

/**
 * UInteger8 type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,UInteger8Type
 * @since 1.2.0
 */
public class UInteger8Encoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        UInteger8Type type = context.getTypeAnnotation(UInteger8Type.class);
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.value(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, int value) {
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + UInteger8Type.SIZE > datagram.length) {
            throw new EncodeException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        } else if (value > UInteger8Type.MAX_VALUE || value < UInteger8Type.MIN_VALUE) {
            throw new EncodeException(
                    MessageFormat.format(CodecError.EXCEEDED_TYPE_SIZE_LIMIT.getMessage(), UInteger8Type.class.getName()));
        }

        datagram[bo] = (byte) value;
    }
}
