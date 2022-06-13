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

import org.indunet.fastproto.annotation.type.BoolType;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * Boolean type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder, BoolType
 * @since 1.0.0
 */
public class BooleanEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        BoolType type = context.getTypeAnnotation(BoolType.class);
        Boolean value = context.getValue(Boolean.class);

        this.encode(context.getDatagram(), type.value(), type.bitOffset(), value);
    }

    public void encode(byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        try {
            CodecUtils.boolType(datagram, byteOffset, bitOffset, value);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding the boolean type.", e);
        } catch (IllegalArgumentException e) {
            throw new EncodingException("Fail encoding the boolean type.", e);
        }
    }
}
