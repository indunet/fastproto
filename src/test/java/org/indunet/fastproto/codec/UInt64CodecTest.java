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

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of uint64 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
class UInt64CodecTest {
    UInt64Codec codec = new UInt64Codec();

    public static List<Arguments> testDecode1() {
        return Stream.of(
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 0, 0}, EndianPolicy.LITTLE, new BigInteger(String.valueOf((long) Math.pow(256, 4)))),
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 0, 1}, EndianPolicy.LITTLE, new BigInteger(String.valueOf((long) Math.pow(256, 4) + (long) Math.pow(256, 7)))),
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 1, 1}, EndianPolicy.BIG, new BigInteger(String.valueOf((long) Math.pow(256, 3) + 1 + 256)))
        ).collect(Collectors.toList());
    }

    public static List<Arguments> testEncode1() {
        return Stream.of(
                Arguments.arguments(new byte[8], 0, EndianPolicy.LITTLE, new BigInteger(String.valueOf(101L)), BinaryUtils.valueOf(101L)),
                Arguments.arguments(new byte[8], -8, EndianPolicy.LITTLE, new BigInteger(String.valueOf(101L)), BinaryUtils.valueOf(101L)),
                Arguments.arguments(new byte[8], 0, EndianPolicy.LITTLE, new BigInteger(String.valueOf(Long.MAX_VALUE)), BinaryUtils.valueOf(Long.MAX_VALUE)),
                Arguments.arguments(new byte[8], 0, EndianPolicy.BIG, new BigInteger(String.valueOf(Integer.MAX_VALUE)),
                        BinaryUtils.valueOf((long) Integer.MAX_VALUE, EndianPolicy.BIG))
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, EndianPolicy policy, BigInteger value) {
        assertEquals(this.codec.decode(datagram, 0, policy), value);
        assertEquals(this.codec.decode(datagram, -8, policy), value);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 0, EndianPolicy.LITTLE));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, -1, EndianPolicy.LITTLE));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10, EndianPolicy.LITTLE));
    }

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, EndianPolicy policy, BigInteger value, byte[] expected) {
        this.codec.encode(datagram, byteOffset, policy, value);

        assertArrayEquals(expected, datagram);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.encode(null, 0, EndianPolicy.BIG, new BigInteger("8")));

        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, -1, EndianPolicy.LITTLE, new BigInteger("-1")));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 8, EndianPolicy.LITTLE, new BigInteger("0")));
    }
}