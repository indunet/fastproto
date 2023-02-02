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
import org.indunet.fastproto.io.ByteBuffer;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.TimeType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
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
            throw new DecodingException("Fail decoding time type.", e);
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
        val order = context.getByteOrder(type::byteOrder);

        return this.decode(bytes, type.offset(), order);
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, Date value) {
        val type = context.getDataTypeAnnotation(TimeType.class);
        val order = context.getByteOrder(type::byteOrder);

        try {
            val millis = value.getTime();

            CodecUtils.int64Type(buffer, type.offset(), order, millis);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding time type.", e);
        }
    }

    public class TimestampCodec implements Codec<Timestamp> {
        @Override
        public Timestamp decode(CodecContext context, byte[] bytes) {
            val date = DateCodec.this.decode(context, bytes);

            return new Timestamp(date.getTime());
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Timestamp value) {
            DateCodec.this.encode(context, buffer, value);
        }
    }

    public class CalendarCodec implements Codec<Calendar> {
        @Override
        public Calendar decode(CodecContext context, byte[] bytes) {
            val date = DateCodec.this.decode(context, bytes);
            val calendar = Calendar.getInstance();

            calendar.setTime(date);

            return calendar;
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Calendar calendar) {
            DateCodec.this.encode(context, buffer, calendar.getTime());
        }
    }

    public class InstantCodec implements Codec<Instant> {
        @Override
        public Instant decode(CodecContext context, byte[] bytes) {
            val date = DateCodec.this.decode(context, bytes);

            return Instant.ofEpochMilli(date.getTime());
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Instant value) {
            DateCodec.this.encode(context, buffer, Date.from(value));
        }
    }

    public class LocalDateTimeCodec implements Codec<LocalDateTime> {
        @Override
        public LocalDateTime decode(CodecContext context, byte[] bytes) {
            val date = DateCodec.this.decode(context, bytes);

            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, LocalDateTime value) {
            val date = Date.from(value.atZone(ZoneId.systemDefault()).toInstant());

            DateCodec.this.encode(context, buffer, date);
        }
    }
}
