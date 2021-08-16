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
import lombok.val;
import org.indunet.fastproto.annotation.type.StringType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.ReverseUtils;

import java.nio.charset.Charset;

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
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);
        int l = ReverseUtils.length(datagram.length, byteOffset, length);

        if (bo < 0) {
            throw new DecodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo > datagram.length) {
            throw new DecodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (l <= 0) {
            throw new DecodeException(CodecError.ILLEGAL_PARAMETER);
        } else if (bo + l > datagram.length) {
            throw new DecodeException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        val bytes = new byte[l];
        System.arraycopy(datagram, bo, bytes, 0, l);

        return new String(bytes, charset);
    }
}
