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
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.util.ReverseUtils;

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
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);
        int l = ReverseUtils.length(datagram.length, byteOffset, length);

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (l <= 0) {
            throw new EncodeException(CodecError.ILLEGAL_PARAMETER);
        } else if (bo + l > datagram.length) {
            throw new EncodeException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        val bytes = value.getBytes(set);

        if (l >= bytes.length) {
            System.arraycopy(bytes, 0, datagram, bo, bytes.length);
        } else {
            System.arraycopy(bytes, 0, datagram, bo, l);
        }
    }
}