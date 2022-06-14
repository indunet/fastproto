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
import org.indunet.fastproto.annotation.type.UInt8Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * UInteger8 type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder, UInt8Type
 * @since 1.2.0
 */
public class UInteger8Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(@NonNull DecodeContext context) {
        UInt8Type type = context.getTypeAnnotation(UInt8Type.class);

        return this.decode(context.getDatagram(), type.offset());
    }

    public int decode(@NonNull final byte[] datagram, int offset) {
        try {
            return CodecUtils.uint8Type(datagram, offset);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding the uinteger8 type.", e);
        } catch (IllegalArgumentException e) {
            throw new DecodingException("Fail decoding the uinteger8 type.", e);
        }
    }
}
