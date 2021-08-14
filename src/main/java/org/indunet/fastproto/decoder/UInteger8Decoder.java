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
import org.indunet.fastproto.annotation.type.UInteger8Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.ReverseUtils;

/**
 * UInteger8 type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,UInteger8Type
 * @since 1.2.0
 */
public class UInteger8Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(@NonNull DecodeContext context) {
        UInteger8Type type = context.getTypeAnnotation(UInteger8Type.class);

        return this.decode(context.getDatagram(), type.value());
    }

    public int decode(@NonNull final byte[] datagram, int byteOffset) {
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new DecodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + UInteger8Type.SIZE > datagram.length) {
            throw new DecodeException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        return datagram[bo] & 0xFF;
    }
}
