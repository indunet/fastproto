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
import org.indunet.fastproto.annotation.type.Int32Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * Integer type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder, Int32Type
 * @since 1.0.0
 */
public class IntegerDecoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(@NonNull DecodeContext context) {
        Int32Type type = context.getTypeAnnotation(Int32Type.class);

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy());
    }

    public int decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy endian) {
        try {
            return CodecUtils.int32Type(datagram, byteOffset, endian);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding integer type.", e);
        }
    }
}
