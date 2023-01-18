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
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of Calendar codec of type time.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class CalendarCodecTest {
    CalendarCodec codec = new CalendarCodec();

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, int byteOffset, ByteOrder policy, Calendar expected) {
        assertEquals(expected, codec.decode(datagram, byteOffset, policy));
    }

    public static List<Arguments> testDecode1() {
        val millis = System.currentTimeMillis();
        val calendar = Calendar.getInstance();

        calendar.setTimeInMillis(millis);

        return Stream.of(
                Arguments.arguments(BinaryUtils.valueOf(millis), 0, ByteOrder.LITTLE, calendar),
                Arguments.arguments(BinaryUtils.valueOf(millis, ByteOrder.BIG), 0, ByteOrder.BIG, calendar)
        ).collect(Collectors.toList());
    }

    @Test
    public void testDecode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 0, ByteOrder.LITTLE));

        assertThrows(DecodingException.class, () -> this.codec.decode(bytes, 10, ByteOrder.LITTLE));
    }

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, ByteOrder policy, Calendar value, byte[] expected) {
        this.codec.encode(datagram, byteOffset, policy, value);
        assertArrayEquals(datagram, expected);
    }

    public static List<Arguments> testEncode1() {
        val millis = System.currentTimeMillis();
        val calendar = Calendar.getInstance();

        calendar.setTimeInMillis(millis);

        return Stream.of(
                Arguments.arguments(new byte[8], 0, ByteOrder.LITTLE, calendar, BinaryUtils.valueOf(millis)),
                Arguments.arguments(new byte[8], -8, ByteOrder.BIG, calendar, BinaryUtils.valueOf(millis, ByteOrder.BIG))
        ).collect(Collectors.toList());
    }

    @Test
    public void testEncode2() {
        val bytes = new byte[10];
        val calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());

        assertThrows(NullPointerException.class,
                () -> this.codec.encode(null, 0, ByteOrder.LITTLE, calendar));
        assertThrows(NullPointerException.class,
                () -> this.codec.encode(null, 0, ByteOrder.LITTLE, null));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(bytes, 10, ByteOrder.LITTLE, calendar));
    }
}
