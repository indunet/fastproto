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
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;
import org.indunet.fastproto.util.ReverseUtils;

/**
 * Long type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder
 * @since 1.0.0
 */
public class LongEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        LongType type = context.getTypeAnnotation(LongType.class);
        Long value = context.getValue(Long.class);

        this.encode(context.getDatagram(), type.value(), context.getEndianPolicy(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, @NonNull EndianPolicy policy, long value) {
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo + LongType.SIZE > datagram.length) {
            throw new SpaceNotEnoughException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (policy == EndianPolicy.BIG) {
            datagram[bo + 7] = (byte) (value & 0xFFL);
            datagram[bo + 6] = (byte) (value >> 8 & 0xFFL);
            datagram[bo + 5] = (byte) (value >> 16 & 0xFFL);
            datagram[bo + 4] = (byte) (value >> 24 & 0xFFL);
            datagram[bo + 3] = (byte) (value >> 32 & 0xFFL);
            datagram[bo + 2] = (byte) (value >> 40 & 0xFFL);
            datagram[bo + 1] = (byte) (value >> 48 & 0xFFL);
            datagram[bo] = (byte) (value >> 56 & 0xFFL);
        } else {
            datagram[bo] = (byte) (value & 0xFFL);
            datagram[bo + 1] = (byte) (value >> 8 & 0xFFL);
            datagram[bo + 2] = (byte) (value >> 16 & 0xFFL);
            datagram[bo + 3] = (byte) (value >> 24 & 0xFFL);
            datagram[bo + 4] = (byte) (value >> 32 & 0xFFL);
            datagram[bo + 5] = (byte) (value >> 40 & 0xFFL);
            datagram[bo + 6] = (byte) (value >> 48 & 0xFFL);
            datagram[bo + 7] = (byte) (value >> 56 & 0xFFL);
        }
    }
}
