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
import org.indunet.fastproto.annotation.type.BooleanType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * Boolean type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,BooleanType
 * @since 1.0.0
 */
public class BooleanDecoder implements TypeDecoder<Boolean> {
    @Override
    public Boolean decode(DecodeContext context) {
        BooleanType type = context.getTypeAnnotation(BooleanType.class);

        return this.decode(context.getDatagram(), type.value(), type.bitOffset());
    }

    public boolean decode(@NonNull final byte[] datagram, int byteOffset, int bitOffset) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (bitOffset > BooleanType.MAX_BIT_OFFSET || bitOffset < BooleanType.MIN_BIT_OFFSET) {
            throw new DecodeException(DecodeError.ILLEGAL_BIT_OFFSET);
        } else if (byteOffset >= datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        } else {
            return (datagram[byteOffset] & (0x01 << bitOffset)) != 0;
        }
    }
}
