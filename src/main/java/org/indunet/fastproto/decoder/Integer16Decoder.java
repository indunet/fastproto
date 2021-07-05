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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.Integer16Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.OutOfBoundsException;
import org.indunet.fastproto.util.ReverseUtils;

/**
 * Integer type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,Integer16Type
 * @since 1.2.0
 */
public class Integer16Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(@NonNull DecodeContext context) {
        Integer16Type type = context.getTypeAnnotation(Integer16Type.class);
        EndianPolicy policy = context.getEndianPolicy();

        return this.decode(context.getDatagram(), type.value(), policy);
    }

    public int decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy policy) {
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new DecodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + Integer16Type.SIZE > datagram.length) {
            throw new OutOfBoundsException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        short value = 0;

        if (policy == EndianPolicy.BIG) {
            value |= (datagram[bo + 1] & 0x00FF);
            value |= (datagram[bo] << 8);
        } else {
            value |= (datagram[bo] & 0x00FF);
            value |= (datagram[bo + 1] << 8);
        }

        return value;
    }
}
