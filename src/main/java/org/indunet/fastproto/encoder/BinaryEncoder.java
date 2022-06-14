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
import lombok.val;
import org.indunet.fastproto.annotation.type.BinaryType;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * Binary type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,BinaryType
 * @since 1.0.0
 */
public class BinaryEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        val type = context.getTypeAnnotation(BinaryType.class);
        val bytes = context.getValue(byte[].class);

        this.encode(context.getDatagram(), type.offset(), type.length(), bytes);
    }

    public void encode(@NonNull byte[] datagram, int offset, int length, @NonNull byte[] bytes) {
        try {
            CodecUtils.binaryType(datagram, offset, length, bytes);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding the binary type.", e);
        } catch (IllegalArgumentException e) {
            throw new EncodingException("Fail encoding the binary type.", e);
        }
    }
}
