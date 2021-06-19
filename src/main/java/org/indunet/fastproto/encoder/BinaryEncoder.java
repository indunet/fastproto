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
import lombok.val;
import org.indunet.fastproto.annotation.type.BinaryType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * Binary type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,BinaryType
 * @since 1.0.0
 */
public class BinaryEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        val type = context.getTypeAnnotation(BinaryType.class);
        val bytes = context.getValue(byte[].class);

        this.encode(context.getDatagram(), type.value(), type.length(), bytes);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, int length, @NonNull byte[] bytes) {
        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;
        int l = length >= 0 ? length : datagram.length + length - bo + 1;

        if (bo < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (bo >= datagram.length) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (l <= 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + l > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (l >= bytes.length) {
            System.arraycopy(bytes, 0, datagram, bo, bytes.length);
        } else {
            System.arraycopy(bytes, 0, datagram, bo, l);
        }
    }
}
