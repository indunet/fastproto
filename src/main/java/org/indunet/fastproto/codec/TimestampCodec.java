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
import org.indunet.fastproto.annotation.TimeType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Timestamp type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class TimestampCodec implements Codec<Timestamp> {
    public Timestamp decode(byte[] datagram, int offset, EndianPolicy policy) {
        try {
            val millis = CodecUtils.int64Type(datagram, offset, policy);

            return new Timestamp(millis);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding time(timestamp) type.", e);
        }
    }

    public void encode(byte[] datagram, int offset, EndianPolicy policy, Timestamp value) {
        try {
            CodecUtils.int64Type(datagram, offset, policy, value.getTime());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding times(timestamp) type.", e);
        }
    }

    @Override
    public Timestamp decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(TimeType.class);
        val policy = Arrays.stream(type.endian())
                .findFirst()
                .orElseGet(context::getDefaultEndianPolicy);

        return this.decode(bytes, type.offset(), policy);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, Timestamp value) {
        val type = context.getDataTypeAnnotation(TimeType.class);
        val policy = Arrays.stream(type.endian())
                .findFirst()
                .orElseGet(context::getDefaultEndianPolicy);

        this.encode(bytes, type.offset(), policy, value);
    }
}
