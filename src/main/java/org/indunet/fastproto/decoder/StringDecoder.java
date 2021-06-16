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
import org.indunet.fastproto.annotation.type.StringType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * String type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.1.0
 */
public class StringDecoder implements TypeDecoder<String> {
    @Override
    public String decode(@NonNull DecodeContext context) {
        StringType type = context.getTypeAnnotation(StringType.class);

        return this.decode(context.getDatagram(), type.value(), type.length(), Charset.forName(type.charsetName()));
    }

    public String decode(@NonNull byte[] datagram, int byteOffset, int length, @NonNull Charset charset) {
        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (bo < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (length < -1) {
            throw new DecodeException(DecodeError.ILLEGAL_PARAMETER);
        } else if (length == -1 && bo >= datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        } else if (length != -1 && bo + length > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (length == -1) {
            return new String(
                    Arrays.copyOfRange(datagram, bo, datagram.length - bo), charset);
        } else {
            return new String(
                    Arrays.copyOfRange(datagram, bo, bo + length), charset);
        }
    }
}
