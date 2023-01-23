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

import java.time.Instant;

/**
 * Instant type codec.
 *
 * @author Deng Ran
 * @since 3.3.1
 */
public class InstantCodec implements Codec<Instant> {
    public Instant decode(final byte[] bytes, int offset, ByteOrder policy) {
        try {
            val millis = CodecUtils.int64Type(bytes, offset, policy);

            return Instant.ofEpochMilli(millis);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding time(Instant) type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, ByteOrder policy, Instant value) {
        try {
            val millis = value.toEpochMilli();

            CodecUtils.int64Type(bytes, offset, policy, millis);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding time(Instant) type.", e);
        }
    }
    
    @Override
    public Instant decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(TimeType.class);
        val order = context.getByteOrder(type::byteOrder);

        return this.decode(bytes, type.offset(), order);
    }
    
    @Override
    public void encode(CodecContext context, byte[] bytes, Instant value) {
        val type = context.getDataTypeAnnotation(TimeType.class);
        val order = context.getByteOrder(type::byteOrder);

        this.encode(bytes, type.offset(), order, value);
    }
}
