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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.Int16Type;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * Integer16 type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder, Int16Type
 * @since 1.2.0
 */
public class Integer16Encoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        Int16Type type = context.getTypeAnnotation(Int16Type.class);
        EndianPolicy policy = context.getEndianPolicy();
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.offset(), policy, value);
    }

    public void encode(@NonNull byte[] datagram, int offset, @NonNull EndianPolicy policy, int value) {
        try {
            CodecUtils.int16Type(datagram, offset, policy, value);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding the integer16 type.", e);
        } catch (IllegalArgumentException e) {
            throw new EncodingException("Fail encoding the integer8 type.", e);
        }
    }
}
