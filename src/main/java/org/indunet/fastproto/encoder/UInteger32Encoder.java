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
import org.indunet.fastproto.annotation.type.UInteger16Type;
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.IllegalValueException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.ReverseUtils;

import java.text.MessageFormat;

/**
 * UInteger32 type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,UInteger32Type
 * @since 1.2.0
 */
public class UInteger32Encoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        UInteger32Type type = context.getTypeAnnotation(UInteger32Type.class);
        EndianPolicy policy = context.getEndianPolicy();
        Long value = context.getValue(Long.class);

        this.encode(context.getDatagram(), type.value(), policy, value);
    }

    public void encode(@NonNull byte[] datagram, int offset, @NonNull EndianPolicy policy, long value) {
        if (value < UInteger32Type.MIN_VALUE || value > UInteger32Type.MAX_VALUE) {
            throw new EncodeException("Fail encoding the integer8 type.");
        }

        try {
            CodecUtils.uinteger32Type(datagram, offset, policy, value);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodeException("Fail encoding the uinteger32 type.", e);
        }
    }
}
