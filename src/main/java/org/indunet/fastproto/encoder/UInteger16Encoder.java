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
import org.indunet.fastproto.annotation.type.UInteger8Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.IllegalValueException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.ReverseUtils;

import java.text.MessageFormat;

/**
 * UInteger16 type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,UInteger16Type
 * @since 1.2.0
 */
public class UInteger16Encoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        UInteger16Type type = context.getTypeAnnotation(UInteger16Type.class);
        EndianPolicy policy = context.getEndianPolicy();
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.value(), policy, value);
    }

    public void encode(@NonNull byte[] datagram, int offset, @NonNull EndianPolicy policy, int value) {
        if (value < UInteger16Type.MIN_VALUE || value > UInteger16Type.MAX_VALUE) {
            throw new EncodeException("Fail encoding the integer8 type.");
        }

        try {
            CodecUtils.uinteger16Type(datagram, offset, policy, value);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodeException("Fail encoding the uinteger16 type.", e);
        }
    }
}