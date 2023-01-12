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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of int32 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class Int32CodecTest {
    private final byte[] datagram = {0, 1, 0, 1, -1, -1, -1, -1};
    Int32Codec codec = new Int32Codec();

    public static List<Arguments> testEncode1() {
        return Stream.of(
                Arguments.arguments(new byte[4], 0, ByteOrder.LITTLE, -101, BinaryUtils.valueOf(-101)),
                Arguments.arguments(new byte[4], -4, ByteOrder.LITTLE, -101, BinaryUtils.valueOf(-101)),
                Arguments.arguments(new byte[4], 0, ByteOrder.BIG, Integer.MAX_VALUE,
                        BinaryUtils.valueOf(Integer.MAX_VALUE, ByteOrder.BIG)),
                Arguments.arguments(new byte[4], 0, ByteOrder.BIG, Integer.MIN_VALUE,
                        BinaryUtils.valueOf(Integer.MIN_VALUE, ByteOrder.BIG))
        ).collect(Collectors.toList());
    }

    @Test
    public void testDecode1() {
        assertEquals(codec.decode(datagram, 0, ByteOrder.LITTLE), 256 + 256 * 256 * 256);
        assertEquals(codec.decode(datagram, 4, ByteOrder.LITTLE), -1);

        assertEquals(codec.decode(datagram, 0, ByteOrder.BIG), 0x00010001);
        assertEquals(codec.decode(datagram, 4, ByteOrder.BIG), -1);
        assertEquals(codec.decode(datagram, 4 - datagram.length, ByteOrder.BIG), -1);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 0, ByteOrder.LITTLE));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, -1, ByteOrder.LITTLE));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10, ByteOrder.LITTLE));
    }

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, ByteOrder policy, int value, byte[] expected) {
        this.codec.encode(datagram, byteOffset, policy, value);

        assertArrayEquals(expected, datagram);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.encode(null, 0, ByteOrder.BIG, 8));

        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, -1, ByteOrder.LITTLE, 11));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 8, ByteOrder.LITTLE, 12));
    }
}
