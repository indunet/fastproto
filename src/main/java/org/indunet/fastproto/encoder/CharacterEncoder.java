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

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.CharacterType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.ReverseUtils;

/**
 * Character type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,CharacterType
 * @since 1.1.0
 */
public class CharacterEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        CharacterType type = context.getTypeAnnotation(CharacterType.class);
        Character value = context.getValue(Character.class);
        EndianPolicy policy = context.getEndianPolicy();

        this.encode(context.getDatagram(), type.value(), policy, value);
    }

    public void encode(byte[] datagram, int offset, EndianPolicy policy, char value) {
        try {
            CodecUtils.characterType(datagram, offset, policy, value);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodeException("Fail encoding the character type.", e);
        }
    }
}
