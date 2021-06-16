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
import org.indunet.fastproto.annotation.type.StringType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.nio.charset.Charset;

/**
 * String type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,StringType
 * @since 1.1.0
 */
public class StringEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        val dataType = context.getTypeAnnotation(StringType.class);
        val value = context.getValue(String.class);

        this.encode(context.getDatagram(), dataType.value(), dataType.length(), Charset.defaultCharset(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, int length, @NonNull Charset set, @NonNull String value) {
        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (bo < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (length < -1) {
            throw new EncodeException(EncodeError.ILLEGAL_PARAMETER);
        }

        val bytes = value.getBytes(set);

        if (length == -1 && bo + bytes.length > datagram.length ) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        } else if (length != -1 && bo + length > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (length == -1) {
            System.arraycopy(bytes, 0, datagram, bo, bytes.length);
        } else {
            System.arraycopy(bytes, 0, datagram, bo, length);
        }
    }
}