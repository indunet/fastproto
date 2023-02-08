/*
 * Copyright 2019-2023 indunet.org
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


package org.indunet.fastproto.util;

import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of decode utils.
 *
 * @author Deng Ran
 * @since 3.10.2
 */
public class DecodeUtilsTest {
    public static List<Arguments> testReadInt64() {
        return Stream.of(
                Arguments.arguments(new byte[]{-1, -1, -1, -1, -1, -1, -1, -1}, ByteOrder.LITTLE, -1L),
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 0, 1}, ByteOrder.LITTLE, (long) Math.pow(256, 4) + (long) Math.pow(256, 7)),
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 0, 1}, ByteOrder.BIG, (long) Math.pow(256, 3) + 1)
        ).collect(Collectors.toList());
    }

    public static List<Arguments> testReadUInt64() {
        return Stream.of(
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 0, 0}, ByteOrder.LITTLE, new BigInteger(String.valueOf((long) Math.pow(256, 4)))),
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 0, 1}, ByteOrder.LITTLE, new BigInteger(String.valueOf((long) Math.pow(256, 4) + (long) Math.pow(256, 7)))),
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 1, 1}, ByteOrder.BIG, new BigInteger(String.valueOf((long) Math.pow(256, 3) + 1 + 256)))
        ).collect(Collectors.toList());
    }

    @Test
    public void testReadBool() {
        byte[] bytes = {0x01, 0x02, 0x04, 0x08};

        assertTrue(DecodeUtils.readBool(bytes, 0, 0, BitOrder.LSB_0));
        assertFalse(DecodeUtils.readBool(bytes, 0, 1, BitOrder.LSB_0));

        assertTrue(DecodeUtils.readBool(bytes, 1, 1, BitOrder.LSB_0));
        assertTrue(DecodeUtils.readBool(bytes, 2, 2, BitOrder.LSB_0));
        assertTrue(DecodeUtils.readBool(bytes, 3, 3, BitOrder.LSB_0));
        assertTrue(DecodeUtils.readBool(bytes, -1, 3, BitOrder.LSB_0));
    }

    @Test
    public void testReadByte() {
        byte[] bytes = {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(DecodeUtils.readByte(bytes, 0), -2);
        assertEquals(DecodeUtils.readByte(bytes, 1), -1);
        assertEquals(DecodeUtils.readByte(bytes, 2), 0);
        assertEquals(DecodeUtils.readByte(bytes, 4), 2);
        assertEquals(DecodeUtils.readByte(bytes, 10), 8);
        assertEquals(DecodeUtils.readByte(bytes, 11), 9);
        assertEquals(DecodeUtils.readByte(bytes, 11 - bytes.length), 9);
    }

    @Test
    public void testReadShort() {
        byte[] bytes = {0, 1, 2, 3, -1, -1, -2, -1};

        // For little endian.
        assertEquals(DecodeUtils.readShort(bytes, 0, ByteOrder.LITTLE), 256);
        assertEquals(DecodeUtils.readShort(bytes, 2, ByteOrder.LITTLE), 2 + 3 * 256);
        assertEquals(DecodeUtils.readShort(bytes, 4, ByteOrder.LITTLE), -1);
        assertEquals(DecodeUtils.readShort(bytes, 6, ByteOrder.LITTLE), -2);

        // For big endian.
        assertEquals(DecodeUtils.readShort(bytes, 0, ByteOrder.BIG), 0x0001);
        assertEquals(DecodeUtils.readShort(bytes, 2, ByteOrder.BIG), 0x0203);
        assertEquals(DecodeUtils.readShort(bytes, 2 - bytes.length, ByteOrder.BIG), 0x0203);
    }

    @Test
    public void testReadInt8() {
        byte[] bytes = new byte[10];

        bytes[0] = -10;
        bytes[1] = 29;

        assertEquals(DecodeUtils.readInt8(bytes, 0), -10);
        assertEquals(DecodeUtils.readInt8(bytes, 1), 29);
        assertEquals(DecodeUtils.readInt8(bytes, 1 - bytes.length), 29);
    }

    @Test
    public void testReadInt16() {
        byte[] bytes = new byte[10];

        // Value 1
        bytes[0] = 1;
        bytes[1] = 2;

        // Value 2
        short value = -29;

        bytes[2] |= (value >> 8);
        bytes[3] |= (value & 0xFF);

        assertEquals(DecodeUtils.readInt16(bytes, 0, ByteOrder.LITTLE), 1 + 256 * 2);
        assertEquals(DecodeUtils.readInt16(bytes, 0, ByteOrder.BIG), 256 + 2);
        assertEquals(DecodeUtils.readInt16(bytes, 2, ByteOrder.BIG), -29);

        assertEquals(DecodeUtils.readInt16(bytes, -8, ByteOrder.BIG), -29);
    }

    @Test
    public void testReadInt32() {
        byte[] bytes = {0, 1, 0, 1, -1, -1, -1, -1};

        assertEquals(DecodeUtils.readInt32(bytes, 0, ByteOrder.LITTLE), 256 + 256 * 256 * 256);
        assertEquals(DecodeUtils.readInt32(bytes, 4, ByteOrder.LITTLE), -1);

        assertEquals(DecodeUtils.readInt32(bytes, 0, ByteOrder.BIG), 0x00010001);
        assertEquals(DecodeUtils.readInt32(bytes, 4, ByteOrder.BIG), -1);
        assertEquals(DecodeUtils.readInt32(bytes, 4 - bytes.length, ByteOrder.BIG), -1);
    }

    @ParameterizedTest
    @MethodSource
    public void testReadInt64(byte[] datagram, ByteOrder policy, long value) {
        assertEquals(DecodeUtils.readInt64(datagram, 0, policy), value);
        assertEquals(DecodeUtils.readInt64(datagram, -8, policy), value);
    }

    @Test
    public void testReadUInt8() {
        byte[] datagram = new byte[10];
        datagram[0] = -10;
        datagram[1] = 29;

        assertEquals(DecodeUtils.readUInt8(datagram, 0), 256 - 10);
        assertEquals(DecodeUtils.readUInt8(datagram, 1), 29);
        assertEquals(DecodeUtils.readUInt8(datagram, 1 - datagram.length), 29);
    }

    @Test
    public void testReadUInt16() {
        byte[] bytes = new byte[10];
        bytes[0] = 1;
        bytes[1] = 2;

        assertEquals(DecodeUtils.readUInt16(bytes, 0, ByteOrder.LITTLE), 1 + 2 * 256);
        assertEquals(DecodeUtils.readUInt16(bytes, 0, ByteOrder.BIG), 256 + 2);

        assertEquals(DecodeUtils.readUInt16(bytes, -10, ByteOrder.BIG), 256 + 2);
    }

    @Test
    public void testReadUInt32() {
        byte[] bytes = new byte[10];

        bytes[0] = 1;
        bytes[1] = 0;
        bytes[2] = 0;
        bytes[3] = 2;

        assertEquals(DecodeUtils.readUInt32(bytes, 0, ByteOrder.LITTLE), 1 + 2 * 256L * 256 * 256);
        assertEquals(DecodeUtils.readUInt32(bytes, 0, ByteOrder.BIG), 256 * 256 * 256 + 2);
        assertEquals(DecodeUtils.readUInt32(bytes, 0 - bytes.length, ByteOrder.BIG), 256 * 256 * 256 + 2);
    }

    @ParameterizedTest
    @MethodSource
    public void testReadUInt64(byte[] datagram, ByteOrder policy, BigInteger value) {
        assertEquals(DecodeUtils.readUInt64(datagram, 0, policy), value);
        assertEquals(DecodeUtils.readUInt64(datagram, -8, policy), value);
    }

    @Test
    public void testReadFloat() {
        float pi = 3.141f;
        float e = 2.718f;

        assertEquals(DecodeUtils.readFloat(BinaryUtils.valueOf(pi), 0, ByteOrder.LITTLE), pi, 0.0001);
        assertEquals(DecodeUtils.readFloat(BinaryUtils.valueOf(e), 0, ByteOrder.LITTLE), e, 0.0001);

        assertEquals(DecodeUtils.readFloat(BinaryUtils.valueOf(e, ByteOrder.BIG), 0, ByteOrder.BIG), e, 0.0001);
        assertEquals(DecodeUtils.readFloat(BinaryUtils.valueOf(e, ByteOrder.BIG), -4, ByteOrder.BIG), e, 0.0001);
    }


    @Test
    public void testReadDouble() {
        double pi = 3.141;
        double e = 2.718;

        assertEquals(DecodeUtils.readDouble(BinaryUtils.valueOf(pi), 0, ByteOrder.LITTLE), pi, 0.001);
        assertEquals(DecodeUtils.readDouble(BinaryUtils.valueOf(e), 0, ByteOrder.LITTLE), e, 0.001);

        assertEquals(DecodeUtils.readDouble(BinaryUtils.valueOf(pi, ByteOrder.BIG), 0, ByteOrder.BIG), pi, 0.001);
        assertEquals(DecodeUtils.readDouble(BinaryUtils.valueOf(pi, ByteOrder.BIG), -8, ByteOrder.BIG), pi, 0.001);
    }

    @Test
    public void testReadBytes() {
        val bytes = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        assertArrayEquals(DecodeUtils.readBytes(bytes, 0, 5), Arrays.copyOfRange(bytes, 0, 5));
    }
}
