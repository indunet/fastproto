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
import org.indunet.fastproto.annotation.type.FloatType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * Float type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,FloatType
 * @since 1.0.0
 */
public class FloatDecoder implements TypeDecoder<Float> {
    @Override
    public Float decode(DecodeContext context) {
        FloatType type = context.getTypeAnnotation(FloatType.class);

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy());
    }

    public float decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy endian) {
        try {
            return CodecUtils.floatType(datagram, byteOffset, endian);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodeException("Fail decoding the float type.", e);
        }
    }
}
