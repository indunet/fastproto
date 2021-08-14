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
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.ReverseUtils;

/**
 * UInteger32 type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,UInteger32Type
 * @since 1.2.0
 */
public class UInteger32Decoder implements TypeDecoder<Long> {
    @Override
    public Long decode(@NonNull DecodeContext context) {
        UInteger32Type type = context.getTypeAnnotation(UInteger32Type.class);
        EndianPolicy policy = context.getEndianPolicy();

        return this.decode(context.getDatagram(), type.value(), policy);
    }

    public long decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy policy) {
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new DecodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + UInteger32Type.SIZE > datagram.length) {
            throw new DecodeException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        long value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[bo] & 0xFF);
            value |= ((datagram[bo + 1] & 0xFFL) << 8);
            value |= ((datagram[bo + 2] & 0xFFL) << 16);
            value |= ((datagram[bo + 3] & 0xFFL) << 24);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[bo + 3] & 0xFF);
            value |= ((datagram[bo + 2] & 0xFFL) << 8);
            value |= ((datagram[bo + 1] & 0xFFL) << 16);
            value |= ((datagram[bo] & 0xFFL) << 24);
        }

        return value;
    }
}
