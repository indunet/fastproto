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
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

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

        this.encode(context.getDatagram(), dataType.offset(), dataType.length(), Charset.defaultCharset(), value);
    }

    public void encode(@NonNull byte[] datagram, int offset, int length, @NonNull Charset set, @NonNull String value) {
        try {
            CodecUtils.binaryType(datagram, offset, length, value.getBytes());
        } catch (IndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding the string type.", e);
        } catch (IllegalArgumentException e) {
            throw new EncodingException("Fail encoding the string type.", e);
        }
    }
}