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
import org.indunet.fastproto.annotation.type.UInt32Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * UInteger32 type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder, UInt32Type
 * @since 1.2.0
 */
public class UInteger32Decoder implements TypeDecoder<Long> {
    @Override
    public Long decode(@NonNull DecodeContext context) {
        UInt32Type type = context.getTypeAnnotation(UInt32Type.class);
        EndianPolicy policy = context.getEndianPolicy();

        return this.decode(context.getDatagram(), type.value(), policy);
    }

    public long decode(@NonNull final byte[] datagram, int offset, @NonNull EndianPolicy policy) {
        try {
            return CodecUtils.uinteger32Type(datagram, offset, policy);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding the uinteger32 type.", e);
        }
    }
}
