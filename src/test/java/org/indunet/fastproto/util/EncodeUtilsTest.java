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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Unit test of encode utils.
 *
 * @author Deng Ran
 * @since 3.10.2
 */
public class EncodeUtilsTest {
    @Test
    public void testWriteBool() {
        byte[] bytes = new byte[10];

        EncodeUtils.writeBool(bytes, 0, 0, BitOrder.LSB_0, true);
        EncodeUtils.writeBool(bytes, 1, 7, BitOrder.LSB_0,true);

        assertEquals(bytes[0], 1);
        assertEquals(bytes[1], -128);

        EncodeUtils.writeBool(bytes, 0, 0, BitOrder.LSB_0,false);
        EncodeUtils.writeBool(bytes, 1 - bytes.length, 7, BitOrder.LSB_0,false);

        assertEquals(bytes[0], 0);
        assertEquals(bytes[1], 0);
    }

    @Test
    public void testWriteByte() {
        byte[] bytes = new byte[10];

        EncodeUtils.writeByte(bytes, 0, (byte) 1);
        EncodeUtils.writeByte(bytes, 1 - bytes.length, (byte) -128);

        assertEquals(bytes[0], 1);
        assertEquals(bytes[1], -128);
    }

    @Test
    public void testWriteShort() {
        val bytes = new byte[4];

        EncodeUtils.writeShort(bytes, 0, ByteOrder.BIG, (short) 0x0102);
        EncodeUtils.writeShort(bytes, 2 - bytes.length, ByteOrder.LITTLE, (short) 0x0304);

        val cache = new byte[]{1, 2, 4, 3};

        assertArrayEquals(bytes, cache);
    }

    @Test
    public void testWriteInt8() {
        byte[] bytes = new byte[10];

        EncodeUtils.writeInt8(bytes, 0, 10);
        EncodeUtils.writeInt8(bytes, 1 - bytes.length, -52);

        assertEquals(bytes[0], 10);
        assertEquals(bytes[1], -52);
    }

    @Test
    public void testWriteInt16() {
        byte[] bytes = new byte[4];

        EncodeUtils.writeInt16(bytes, 0, ByteOrder.LITTLE, 0x0102);
        EncodeUtils.writeInt16(bytes, 2 - bytes.length, ByteOrder.BIG, -300);

        byte[] cache = new byte[4];

        cache[0] = 2;
        cache[1] = 1;
        cache[2] |= ((short) -300 >> 8);
        cache[3] |= ((short) -300 & 0xFF);

        assertArrayEquals(bytes, cache);
    }

    public static List<Arguments> testWriteInt32() {
        return Stream.of(
                Arguments.arguments(new byte[4], 0, ByteOrder.LITTLE, -101, BinaryUtils.valueOf(-101)),
                Arguments.arguments(new byte[4], -4, ByteOrder.LITTLE, -101, BinaryUtils.valueOf(-101)),
                Arguments.arguments(new byte[4], 0, ByteOrder.BIG, Integer.MAX_VALUE,
                        BinaryUtils.valueOf(Integer.MAX_VALUE, ByteOrder.BIG)),
                Arguments.arguments(new byte[4], 0, ByteOrder.BIG, Integer.MIN_VALUE,
                        BinaryUtils.valueOf(Integer.MIN_VALUE, ByteOrder.BIG))
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testWriteInt32(byte[] bytes, int byteOffset, ByteOrder policy, int value, byte[] expected) {
        EncodeUtils.writeInt32(bytes, byteOffset, policy, value);

        assertArrayEquals(expected, bytes);
    }

    public static List<Arguments> testWriteInt64() {
        return Stream.of(
                Arguments.arguments(new byte[8], 0, ByteOrder.LITTLE, -101L, BinaryUtils.valueOf(-101L)),
                Arguments.arguments(new byte[8], -8, ByteOrder.LITTLE, -101L, BinaryUtils.valueOf(-101L)),
                Arguments.arguments(new byte[8], 0, ByteOrder.BIG, (long) Integer.MAX_VALUE,
                        BinaryUtils.valueOf((long) Integer.MAX_VALUE, ByteOrder.BIG))
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testWriteInt64(byte[] bytes, int byteOffset, ByteOrder policy, long value, byte[] expected) {
        EncodeUtils.writeInt64(bytes, byteOffset, policy, value);

        assertArrayEquals(expected, bytes);
    }

    @Test
    public void testWriteUInt8() {
        byte[] bytes = new byte[2];

        EncodeUtils.writeUInt8(bytes, 0, 1);
        EncodeUtils.writeUInt8(bytes, 1 - bytes.length, 255);

        byte[] cache = new byte[]{1, -1};

        assertArrayEquals(bytes, cache);
    }

    @Test
    public void testWriteUInt16() {
        byte[] bytes = new byte[4];

        EncodeUtils.writeUInt16(bytes, 0, ByteOrder.LITTLE, 0x0102);
        EncodeUtils.writeUInt16(bytes, 2 - bytes.length, ByteOrder.BIG, 0x0304);

        byte[] cache = new byte[]{0x02, 0x01, 0x03, 0x04};

        assertArrayEquals(bytes, cache);
    }

    @Test
    public void testWriteUInt32() {
        byte[] bytes = new byte[8];

        EncodeUtils.writeUInt32(bytes, 0, ByteOrder.LITTLE, 0x01020304);
        EncodeUtils.writeUInt32(bytes, 4 - bytes.length, ByteOrder.BIG, 0x05060708);

        byte[] cache = new byte[]{0x04, 0x03, 0x02, 0x01, 0x05, 0x06, 0x07, 0x08};

        assertArrayEquals(bytes, cache);
    }

    public static List<Arguments> testWriteUInt64() {
        return Stream.of(
                Arguments.arguments(new byte[8], 0, ByteOrder.LITTLE, new BigInteger(String.valueOf(101L)), BinaryUtils.valueOf(101L)),
                Arguments.arguments(new byte[8], -8, ByteOrder.LITTLE, new BigInteger(String.valueOf(101L)), BinaryUtils.valueOf(101L)),
                Arguments.arguments(new byte[8], 0, ByteOrder.LITTLE, new BigInteger(String.valueOf(Long.MAX_VALUE)), BinaryUtils.valueOf(Long.MAX_VALUE)),
                Arguments.arguments(new byte[8], 0, ByteOrder.BIG, new BigInteger(String.valueOf(Integer.MAX_VALUE)),
                        BinaryUtils.valueOf((long) Integer.MAX_VALUE, ByteOrder.BIG))
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testWriteUInt64(byte[] bytes, int byteOffset, ByteOrder policy, BigInteger value, byte[] expected) {
        EncodeUtils.writeUInt64(bytes, byteOffset, policy, value);

        assertArrayEquals(expected, bytes);
    }

    @Test
    public void testWriteFloat() {
        byte[] bytes = new byte[4];
        float pi = 3.141f;
        float e = 2.718f;

        EncodeUtils.writeFloat(bytes, 0, ByteOrder.LITTLE, pi);
        assertArrayEquals(bytes, BinaryUtils.valueOf(pi));

        EncodeUtils.writeFloat(bytes, 0, ByteOrder.LITTLE, e);
        assertArrayEquals(bytes, BinaryUtils.valueOf(e));

        EncodeUtils.writeFloat(bytes, 0 - bytes.length, ByteOrder.BIG, e);
        assertArrayEquals(bytes, BinaryUtils.valueOf(e, ByteOrder.BIG));
    }

    @Test
    public void testWriteDouble() {
        byte[] bytes = new byte[8];
        double pi = 3.141;
        double e = 2.718;

        EncodeUtils.writeDouble(bytes, 0, ByteOrder.LITTLE, e);
        assertArrayEquals(bytes, BinaryUtils.valueOf(e));

        EncodeUtils.writeDouble(bytes, 0, ByteOrder.LITTLE, pi);
        assertArrayEquals(bytes, BinaryUtils.valueOf(pi));

        EncodeUtils.writeDouble(bytes, 0 - bytes.length, ByteOrder.BIG, pi);
        assertArrayEquals(bytes, BinaryUtils.valueOf(pi, ByteOrder.BIG));
    }

    @Test
    public void testWriteBytes() {
        val bytes = new byte[] {1, 2, 3, 4, 5};
        val actuals = new byte[10];

        EncodeUtils.writeBytes(actuals, 2, bytes);
        assertArrayEquals(new byte[] {0, 0, 1, 2, 3, 4, 5, 0, 0, 0}, actuals);
    }
}
