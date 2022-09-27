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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.TimeType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.util.Date;

/**
 * Date type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class DateCodec implements Codec<Date> {
    public Date decode(final byte[] bytes, int offset, EndianPolicy policy) {
        try {
            val millis = CodecUtils.int64Type(bytes, offset, policy);

            return new Date(millis);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding time(date) type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, EndianPolicy policy, Date value) {
        try {
            val millis = value.getTime();

            CodecUtils.int64Type(bytes, offset, policy, millis);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding time(date) type.", e);
        }
    }
    
    @Override
    public Date decode(CodecContext context, byte[] bytes) {
        val policy = context.getEndianPolicy();
        val type = context.getDataTypeAnnotation(TimeType.class);
    
        return this.decode(bytes, type.offset(), policy);
    }
    
    @Override
    public void encode(CodecContext context, byte[] bytes, Date value) {
        val policy = context.getEndianPolicy();
        val type = context.getDataTypeAnnotation(TimeType.class);
    
        this.encode(bytes, type.offset(), policy, value);
    }
}
