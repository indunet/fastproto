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

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of Instant type codec.
 *
 * @author Deng Ran
 * @since 3.3.1
 */
public class InstantCodecTest {
    InstantCodec codec = new InstantCodec();

    public static List<Arguments> testDecode1() {
        long current = System.currentTimeMillis();

        return Stream.of(
                Arguments.arguments(BinaryUtils.valueOf(current), 0, ByteOrder.LITTLE, Instant.ofEpochMilli(current)),
                Arguments.arguments(BinaryUtils.valueOf(current, ByteOrder.BIG), 0, ByteOrder.BIG, Instant.ofEpochMilli(current))
        ).collect(Collectors.toList());
    }

    public static List<Arguments> testEncode1() {
        long current = System.currentTimeMillis();

        return Stream.of(
                Arguments.arguments(new byte[8], 0, ByteOrder.LITTLE, Instant.ofEpochMilli(current), BinaryUtils.valueOf(current)),
                Arguments.arguments(new byte[8], -8, ByteOrder.BIG, Instant.ofEpochMilli(current), BinaryUtils.valueOf(current, ByteOrder.BIG))
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, int byteOffset, ByteOrder policy, Instant expected) {
        assertEquals(expected, codec.decode(datagram, byteOffset, policy));
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 0, ByteOrder.LITTLE));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10, ByteOrder.LITTLE));
    }

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, ByteOrder policy, Instant value, byte[] expected) {
        this.codec.encode(datagram, byteOffset, policy, value);

        assertArrayEquals(datagram, expected);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.encode(null, 0, ByteOrder.LITTLE, Instant.ofEpochMilli(System.currentTimeMillis())));
        assertThrows(NullPointerException.class,
                () -> this.codec.encode(null, 0, ByteOrder.LITTLE, null));

        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, -1, ByteOrder.LITTLE, Instant.ofEpochMilli(System.currentTimeMillis())));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 10, ByteOrder.LITTLE, Instant.ofEpochMilli(System.currentTimeMillis())));
    }
}