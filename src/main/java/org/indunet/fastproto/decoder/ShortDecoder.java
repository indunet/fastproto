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
import org.indunet.fastproto.annotation.type.ShortType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * Short type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.0.0
 */
public class ShortDecoder implements TypeDecoder<Short> {
    @Override
    public Short decode(@NonNull DecodeContext context) {
        ShortType type = context.getTypeAnnotation(ShortType.class);

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy());
    }

    public short decode(@NonNull final byte[] datagram, int offset, @NonNull EndianPolicy policy) {
        try {
            return CodecUtils.shortType(datagram, offset, policy);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodeException("Fail decoding the short type.", e);
        }
    }
}
