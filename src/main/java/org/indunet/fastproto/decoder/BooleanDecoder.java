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
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.CodecUtils;

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
        try {
            return CodecUtils.booleanType(datagram, byteOffset, bitOffset);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding the boolean type.", e);
        } catch (IllegalArgumentException e) {
            throw new DecodingException("Fail decoding the boolean type.", e);
        }
    }
}
