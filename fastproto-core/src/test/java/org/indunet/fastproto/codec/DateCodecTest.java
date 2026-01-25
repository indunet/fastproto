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
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.AnnotationUtils;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of Date type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class DateCodecTest {
    DateCodec dateCodec = new DateCodec();

    public static List<Arguments> testDecode1() {
        long current = System.currentTimeMillis();

        return Stream.of(
                Arguments.arguments(BinaryUtils.valueOf(current), 0, ByteOrder.LITTLE, new Date(current)),
                Arguments.arguments(BinaryUtils.valueOf(current, ByteOrder.BIG), 0, ByteOrder.BIG, new Date(current))
        ).collect(Collectors.toList());
    }

    public static List<Arguments> testEncode1() {
        long current = System.currentTimeMillis();

        return Stream.of(
                Arguments.arguments(new byte[8], 0, ByteOrder.LITTLE, new Date(current), BinaryUtils.valueOf(current)),
                Arguments.arguments(new byte[8], -8, ByteOrder.BIG, new Date(current), BinaryUtils.valueOf(current, ByteOrder.BIG))
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] bytes, int offset, ByteOrder order, Date expected) {
        assertEquals(expected, dateCodec.decode(mock(offset, order), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testDecode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class, () -> this.dateCodec.decode(mock(0, ByteOrder.LITTLE), null));
        assertThrows(DecodingException.class, () -> this.dateCodec.decode(mock(10, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
    }

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] bytes, int offset, ByteOrder order, Date value, byte[] expected) {
        this.dateCodec.encode(mock(offset, order), new ByteBufferOutputStream(bytes), value);

        assertArrayEquals(expected, bytes);
    }

    @Test
    public void testEncode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.dateCodec.encode(mock(0, ByteOrder.LITTLE), null, new Date(System.currentTimeMillis())));
        assertThrows(NullPointerException.class,
                () -> this.dateCodec.encode(mock(0, ByteOrder.LITTLE), null, null));

        assertThrows(EncodingException.class,
                () -> this.dateCodec.encode(mock(-1, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), new Date(System.currentTimeMillis())));
        assertThrows(EncodingException.class,
                () -> this.dateCodec.encode(mock(10, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), new Date(System.currentTimeMillis())));
    }

    @Test
    public void testCalendar() {
        val calendarCodec = dateCodec.new CalendarCodec();
        val calendar = Calendar.getInstance();
        val outputStream = new ByteBufferOutputStream();

        outputStream.writeInt64(ByteOrder.LITTLE, calendar.getTimeInMillis());

        val bytes = outputStream.toByteBuffer().toBytes();
        val actual = calendarCodec.decode(mock(0, ByteOrder.LITTLE), new ByteBufferInputStream(bytes));

        assertEquals(calendar, actual);

        val actuals = new byte[8];
        calendarCodec.encode(mock(0, ByteOrder.LITTLE), new ByteBufferOutputStream(actuals), calendar);

        assertArrayEquals(bytes, actuals);
    }

    @Test
    public void testInstant() {
        val instantCodec = dateCodec.new InstantCodec();
        val instant = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        val outputStream = new ByteBufferOutputStream();

        outputStream.writeInt64(ByteOrder.BIG, instant.toEpochMilli());

        val bytes = outputStream.toByteBuffer().toBytes();
        val actual = instantCodec.decode(mock(0, ByteOrder.BIG), new ByteBufferInputStream(bytes));

        assertEquals(instant, actual);

        val actuals = new byte[8];
        instantCodec.encode(mock(0, ByteOrder.BIG), new ByteBufferOutputStream(actuals), instant);

        assertArrayEquals(bytes, actuals);
    }

    @Test
    public void testTimestamp() {
        val timestampCodec = dateCodec.new TimestampCodec();
        val timestamp = new Timestamp(System.currentTimeMillis());
        val outputStream = new ByteBufferOutputStream();

        outputStream.writeInt64(ByteOrder.BIG, timestamp.getTime());

        val bytes = outputStream.toByteBuffer().toBytes();
        val actual = timestampCodec.decode(mock(0, ByteOrder.BIG), new ByteBufferInputStream(bytes));

        assertEquals(timestamp, actual);

        val actuals = new byte[8];
        timestampCodec.encode(mock(0, ByteOrder.BIG), new ByteBufferOutputStream(actuals), timestamp);

        assertArrayEquals(bytes, actuals);
    }

    @Test
    public void testLocalDateTime() {
        val localDateTimeCodec = dateCodec.new LocalDateTimeCodec();
        val localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        val outputStream = new ByteBufferOutputStream();

        outputStream.writeInt64(ByteOrder.BIG, localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        val bytes = outputStream.toByteBuffer().toBytes();
        val actual = localDateTimeCodec.decode(mock(0, ByteOrder.BIG), new ByteBufferInputStream(bytes));

        assertEquals(localDateTime, actual);

        val actuals = new byte[8];
        localDateTimeCodec.encode(mock(0, ByteOrder.BIG), new ByteBufferOutputStream(actuals), localDateTime);

        assertArrayEquals(bytes, actuals);
    }

    protected CodecContext mock(int offset, ByteOrder order) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(TimeType.class, offset, order))
                .build();
    }
}