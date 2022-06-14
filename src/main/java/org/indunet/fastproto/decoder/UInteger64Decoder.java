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
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.UInt64Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.math.BigInteger;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class UInteger64Decoder implements TypeDecoder<BigInteger> {
    @Override
    public BigInteger decode(DecodeContext context) {
        val type = context.getTypeAnnotation(UInt64Type.class);

        return this.decode(context.getDatagram(), type.offset(), context.getEndianPolicy());
    }

    public BigInteger decode(@NonNull final byte[] datagram, int offset, @NonNull EndianPolicy policy) {
        try {
            return CodecUtils.uint64Type(datagram, offset, policy);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding the uinteger64 type.", e);
        }
    }
}
