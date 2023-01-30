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
import org.indunet.fastproto.ByteBuffer;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.TimeType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * LocalDateTime type codec
 *
 * @author Deng Ran
 * @since 3.9.1
 */
public class LocalDateTimeCodec implements Codec<LocalDateTime> {
    public LocalDateTime decode(final byte[] bytes, int offset, ByteOrder byteOrder) {
        try {
            val millis = CodecUtils.int64Type(bytes, offset, byteOrder);
            val instant = Instant.ofEpochMilli(millis);

            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding time(LocalDateTime) type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, ByteOrder byteOrder, LocalDateTime value) {
        try {
            val zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            val millis = value.toInstant(zoneOffset).toEpochMilli();

            CodecUtils.int64Type(bytes, offset, byteOrder, millis);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding time(LocalDateTime) type.", e);
        }
    }

    @Override
    public LocalDateTime decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(TimeType.class);
        val order = context.getByteOrder(type::byteOrder);

        return this.decode(bytes, type.offset(), order);
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, LocalDateTime value) {
        val type = context.getDataTypeAnnotation(TimeType.class);
        val order = context.getByteOrder(type::byteOrder);

        try {
            val zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            val millis = value.toInstant(zoneOffset).toEpochMilli();

            CodecUtils.int64Type(buffer, type.offset(), order, millis);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding time(LocalDateTime) type.", e);
        }
    }
}
