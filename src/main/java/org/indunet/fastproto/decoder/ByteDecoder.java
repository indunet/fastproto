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
import org.indunet.fastproto.annotation.type.ByteType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * Byte type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,ByteType
 * @since 1.0.0
 */
public class ByteDecoder implements TypeDecoder<Byte> {
    @Override
    public Byte decode(DecodeContext context) {
        ByteType type = context.getTypeAnnotation(ByteType.class);

        return this.decode(context.getDatagram(), type.offset());
    }

    public byte decode(@NonNull final byte[] datagram, int offset) {
        try {
            return CodecUtils.byteType(datagram, offset);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding the byte type.", e);
        } catch (IllegalArgumentException e) {
            throw new DecodingException("Fail decoding the byte type.", e);
        }
    }
}
