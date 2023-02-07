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
import org.indunet.fastproto.annotation.TimeType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

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
    @Override
    public Date decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(TimeType.class);
            val order = context.getByteOrder(type::byteOrder);
            val millis = inputStream.readInt64(type.offset(), order);

            return new Date(millis);
        } catch (IndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding time type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, Date value) {
        try {
            val type = context.getDataTypeAnnotation(TimeType.class);
            val order = context.getByteOrder(type::byteOrder);
            val millis = value.getTime();

            outputStream.writeInt64(type.offset(), order, millis);
        } catch (IndexOutOfBoundsException e) {
            throw new EncodingException("Fail encoding time type.", e);
        }
    }

    public class TimestampCodec implements Codec<Timestamp> {
        @Override
        public Timestamp decode(CodecContext context, ByteBufferInputStream inputStream) {
            val date = DateCodec.this.decode(context, inputStream);

            return new Timestamp(date.getTime());
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Timestamp value) {
            DateCodec.this.encode(context, outputStream, value);
        }
    }

    public class CalendarCodec implements Codec<Calendar> {
        @Override
        public Calendar decode(CodecContext context, ByteBufferInputStream inputStream) {
            val date = DateCodec.this.decode(context, inputStream);
            val calendar = Calendar.getInstance();

            calendar.setTime(date);

            return calendar;
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Calendar calendar) {
            DateCodec.this.encode(context, outputStream, calendar.getTime());
        }
    }

    public class InstantCodec implements Codec<Instant> {
        @Override
        public Instant decode(CodecContext context, ByteBufferInputStream inputStream) {
            val date = DateCodec.this.decode(context, inputStream);

            return Instant.ofEpochMilli(date.getTime());
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Instant value) {
            DateCodec.this.encode(context, outputStream, Date.from(value));
        }
    }

    public class LocalDateTimeCodec implements Codec<LocalDateTime> {
        @Override
        public LocalDateTime decode(CodecContext context, ByteBufferInputStream inputStream) {
            val date = DateCodec.this.decode(context, inputStream);

            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, LocalDateTime value) {
            val date = Date.from(value.atZone(ZoneId.systemDefault()).toInstant());

            DateCodec.this.encode(context, outputStream, date);
        }
    }
}
