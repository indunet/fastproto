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

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of Timestamp type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class TimestampCodecTest {
    TimestampCodec codec = new TimestampCodec();

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, int byteOffset, EndianPolicy policy, Timestamp expected) {
        assertEquals(expected, codec.decode(datagram, byteOffset, policy));
    }

    public static List<Arguments> testDecode1() {
        long current = System.currentTimeMillis();

        return Stream.of(
                Arguments.arguments(BinaryUtils.valueOf(current), 0, EndianPolicy.LITTLE, new Timestamp(current)),
                Arguments.arguments(BinaryUtils.valueOf(current, EndianPolicy.BIG), 0, EndianPolicy.BIG, new Timestamp(current))
        ).collect(Collectors.toList());
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 0, EndianPolicy.LITTLE));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10, EndianPolicy.LITTLE));
    }

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, EndianPolicy policy, Timestamp value, byte[] expected) {
        this.codec.encode(datagram, byteOffset, policy, value);
        assertArrayEquals(datagram, expected);
    }

    public static List<Arguments> testEncode1() {
        long current = System.currentTimeMillis();

        return Stream.of(
                Arguments.arguments(new byte[8], 0, EndianPolicy.LITTLE, new Timestamp(current), BinaryUtils.valueOf(current)),
                Arguments.arguments(new byte[8], -8, EndianPolicy.BIG, new Timestamp(current), BinaryUtils.valueOf(current, EndianPolicy.BIG))
        ).collect(Collectors.toList());
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.encode(null, 0, EndianPolicy.LITTLE, new Timestamp(System.currentTimeMillis())));
        assertThrows(NullPointerException.class,
                () -> this.codec.encode(null, 0, EndianPolicy.LITTLE, null));

        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 10, EndianPolicy.LITTLE, new Timestamp(System.currentTimeMillis())));
    }
}