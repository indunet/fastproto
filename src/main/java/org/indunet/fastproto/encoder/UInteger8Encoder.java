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
import org.indunet.fastproto.annotation.type.UInt8Type;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * UInteger8 type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder, UInt8Type
 * @since 1.2.0
 */
public class UInteger8Encoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        UInt8Type type = context.getTypeAnnotation(UInt8Type.class);
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.value(), value);
    }

    public void encode(@NonNull byte[] datagram, int offset, int value) {
        try {
            CodecUtils.uint8Type(datagram, offset, value);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding the uinteger8 type.", e);
        } catch (IllegalArgumentException e) {
            throw new EncodingException("Fail encoding the uinteger8 type.", e);
        }
    }
}
