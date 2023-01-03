/*
 * Copyright 2019-2022 indunet.org
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

package org.indunet.fastproto.codec;

import lombok.val;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.TimeType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.util.Arrays;
import java.util.Date;

/**
 * Date type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class DateCodec implements Codec<Date> {
    public Date decode(final byte[] bytes, int offset, ByteOrder byteOrder) {
        try {
            val millis = CodecUtils.int64Type(bytes, offset, byteOrder);

            return new Date(millis);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding time(date) type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, ByteOrder policy, Date value) {
        try {
            val millis = value.getTime();

            CodecUtils.int64Type(bytes, offset, policy, millis);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding time(date) type.", e);
        }
    }
    
    @Override
    public Date decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(TimeType.class);
        val byteOrder = Arrays.stream(type.byteOrder())
                .findFirst()
                .orElseGet(context::getDefaultByteOrder);

        return this.decode(bytes, type.offset(), byteOrder);
    }
    
    @Override
    public void encode(CodecContext context, byte[] bytes, Date value) {
        val type = context.getDataTypeAnnotation(TimeType.class);
        val byteOrder = Arrays.stream(type.byteOrder())
                .findFirst()
                .orElseGet(context::getDefaultByteOrder);

        this.encode(bytes, type.offset(), byteOrder, value);
    }
}
