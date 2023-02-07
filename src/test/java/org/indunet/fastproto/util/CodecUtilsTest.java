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

/**
 * Unit test of codec utils.
 *
 * @author Deng Ran
 * @since 3.9.2
 */
public class CodecUtilsTest {
    @Test
    public void testDecodeBool() {
        byte[] datagram = {0x01, 0x02, 0x04, 0x08};

        assertTrue(CodecUtils.boolType(datagram, 0, 0, BitOrder.LSB_0));
        assertFalse(CodecUtils.boolType(datagram, 0, 1, BitOrder.LSB_0));

        assertTrue(CodecUtils.boolType(datagram, 1, 1, BitOrder.LSB_0));
        assertTrue(CodecUtils.boolType(datagram, 2, 2, BitOrder.LSB_0));
        assertTrue(CodecUtils.boolType(datagram, 3, 3, BitOrder.LSB_0));
        assertTrue(CodecUtils.boolType(datagram, -1, 3, BitOrder.LSB_0));
    }

    @Test
    public void testEncodeBool() {
        byte[] datagram = new byte[10];

        CodecUtils.boolType(datagram, 0, 0, BitOrder.LSB_0, true);
        CodecUtils.boolType(datagram, 1, 7, BitOrder.LSB_0,true);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);

        CodecUtils.boolType(datagram, 0, 0, BitOrder.LSB_0,false);
        CodecUtils.boolType(datagram, 1 - datagram.length, 7, BitOrder.LSB_0,false);

        assertEquals(datagram[0], 0);
        assertEquals(datagram[1], 0);
    }

    @Test
    public void testDecodeByte() {
        byte[] datagram = {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(CodecUtils.byteType(datagram, 0), -2);
        assertEquals(CodecUtils.byteType(datagram, 1), -1);
        assertEquals(CodecUtils.byteType(datagram, 2), 0);
        assertEquals(CodecUtils.byteType(datagram, 4), 2);
        assertEquals(CodecUtils.byteType(datagram, 10), 8);
        assertEquals(CodecUtils.byteType(datagram, 11), 9);
        assertEquals(CodecUtils.byteType(datagram, 11 - datagram.length), 9);
    }

    @Test
    public void testEncodeByte() {
        byte[] datagram = new byte[10];

        CodecUtils.byteType(datagram, 0, (byte) 1);
        CodecUtils.byteType(datagram, 1 - datagram.length, (byte) -128);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);
    }

    @Test
    public void testDecodeShort() {
        byte[] datagram = {0, 1, 2, 3, -1, -1, -2, -1};

        // For little endian.
        assertEquals(CodecUtils.shortType(datagram, 0, ByteOrder.LITTLE), 256);
        assertEquals(CodecUtils.shortType(datagram, 2, ByteOrder.LITTLE), 2 + 3 * 256);
        assertEquals(CodecUtils.shortType(datagram, 4, ByteOrder.LITTLE), -1);
        assertEquals(CodecUtils.shortType(datagram, 6, ByteOrder.LITTLE), -2);

        // For big endian.
        assertEquals(CodecUtils.shortType(datagram, 0, ByteOrder.BIG), 0x0001);
        assertEquals(CodecUtils.shortType(datagram, 2, ByteOrder.BIG), 0x0203);
        assertEquals(CodecUtils.shortType(datagram, 2 - datagram.length, ByteOrder.BIG), 0x0203);
    }

    @Test
    public void testEncodeShort() {
        val datagram = new byte[4];

        CodecUtils.shortType(datagram, 0, ByteOrder.BIG, (short) 0x0102);
        CodecUtils.shortType(datagram, 2 - datagram.length, ByteOrder.LITTLE, (short) 0x0304);

        val cache = new byte[]{1, 2, 4, 3};

        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testDecodeInt8() {
        byte[] datagram = new byte[10];
        datagram[0] = -10;
        datagram[1] = 29;

        assertEquals(CodecUtils.int8Type(datagram, 0), -10);
        assertEquals(CodecUtils.int8Type(datagram, 1), 29);
        assertEquals(CodecUtils.int8Type(datagram, 1 - datagram.length), 29);
    }

    @Test
    public void testEncodeInt8() {
        byte[] datagram = new byte[10];

        CodecUtils.int8Type(datagram, 0, 10);
        CodecUtils.int8Type(datagram, 1 - datagram.length, -52);

        assertEquals(datagram[0], 10);
        assertEquals(datagram[1], -52);
    }

    @Test
    public void testDecodeInt16() {
        byte[] datagram = new byte[10];

        // Value 1
        datagram[0] = 1;
        datagram[1] = 2;

        // Value 2
        short value = -29;
        datagram[2] |= (value >> 8);
        datagram[3] |= (value & 0xFF);

        assertEquals(CodecUtils.int16Type(datagram, 0, ByteOrder.LITTLE), 1 + 256 * 2);
        assertEquals(CodecUtils.int16Type(datagram, 0, ByteOrder.BIG), 256 + 2);
        assertEquals(CodecUtils.int16Type(datagram, 2, ByteOrder.BIG), -29);

        assertEquals(CodecUtils.int16Type(datagram, -8, ByteOrder.BIG), -29);
    }

    @Test
    public void testEncodeInt16() {
        byte[] datagram = new byte[4];

        CodecUtils.int16Type(datagram, 0, ByteOrder.LITTLE, 0x0102);
        CodecUtils.int16Type(datagram, 2 - datagram.length, ByteOrder.BIG, -300);

        byte[] cache = new byte[4];
        cache[0] = 2;
        cache[1] = 1;
        cache[2] |= ((short) -300 >> 8);
        cache[3] |= ((short) -300 & 0xFF);

        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testDecodeInt32() {
        byte[] bytes = {0, 1, 0, 1, -1, -1, -1, -1};

        assertEquals(CodecUtils.int32Type(bytes, 0, ByteOrder.LITTLE), 256 + 256 * 256 * 256);
        assertEquals(CodecUtils.int32Type(bytes, 4, ByteOrder.LITTLE), -1);

        assertEquals(CodecUtils.int32Type(bytes, 0, ByteOrder.BIG), 0x00010001);
        assertEquals(CodecUtils.int32Type(bytes, 4, ByteOrder.BIG), -1);
        assertEquals(CodecUtils.int32Type(bytes, 4 - bytes.length, ByteOrder.BIG), -1);
    }

    public static List<Arguments> testEncodeInt32() {
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
    public void testEncodeInt32(byte[] datagram, int byteOffset, ByteOrder policy, int value, byte[] expected) {
        CodecUtils.int32Type(datagram, byteOffset, policy, value);

        assertArrayEquals(expected, datagram);
    }

    public static List<Arguments> testDecodeInt64() {
        return Stream.of(
                Arguments.arguments(new byte[]{-1, -1, -1, -1, -1, -1, -1, -1}, ByteOrder.LITTLE, -1L),
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 0, 1}, ByteOrder.LITTLE, (long) Math.pow(256, 4) + (long) Math.pow(256, 7)),
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 0, 1}, ByteOrder.BIG, (long) Math.pow(256, 3) + 1)
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testDecodeInt64(byte[] datagram, ByteOrder policy, long value) {
        assertEquals(CodecUtils.int64Type(datagram, 0, policy), value);
        assertEquals(CodecUtils.int64Type(datagram, -8, policy), value);
    }

    public static List<Arguments> testEncodeInt64() {
        return Stream.of(
                Arguments.arguments(new byte[8], 0, ByteOrder.LITTLE, -101L, BinaryUtils.valueOf(-101L)),
                Arguments.arguments(new byte[8], -8, ByteOrder.LITTLE, -101L, BinaryUtils.valueOf(-101L)),
                Arguments.arguments(new byte[8], 0, ByteOrder.BIG, (long) Integer.MAX_VALUE,
                        BinaryUtils.valueOf((long) Integer.MAX_VALUE, ByteOrder.BIG))
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testEncodeInt64(byte[] datagram, int byteOffset, ByteOrder policy, long value, byte[] expected) {
        CodecUtils.int64Type(datagram, byteOffset, policy, value);

        assertArrayEquals(expected, datagram);
    }

    @Test
    public void testDecodeUInt8() {
        byte[] datagram = new byte[10];
        datagram[0] = -10;
        datagram[1] = 29;

        assertEquals(CodecUtils.uint8Type(datagram, 0), 256 - 10);
        assertEquals(CodecUtils.uint8Type(datagram, 1), 29);
        assertEquals(CodecUtils.uint8Type(datagram, 1 - datagram.length), 29);
    }

    @Test
    public void testEncodeUInt8() {
        byte[] datagram = new byte[2];

        CodecUtils.uint8Type(datagram, 0, 1);
        CodecUtils.uint8Type(datagram, 1 - datagram.length, 255);

        byte[] cache = new byte[]{1, -1};

        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testDecodeUInt16() {
        byte[] datagram = new byte[10];
        datagram[0] = 1;
        datagram[1] = 2;

        assertEquals(CodecUtils.uint16Type(datagram, 0, ByteOrder.LITTLE), 1 + 2 * 256);
        assertEquals(CodecUtils.uint16Type(datagram, 0, ByteOrder.BIG), 256 + 2);

        assertEquals(CodecUtils.uint16Type(datagram, -10, ByteOrder.BIG), 256 + 2);
    }

    @Test
    public void testEncodeUInt16() {
        byte[] datagram = new byte[4];

        CodecUtils.uint16Type(datagram, 0, ByteOrder.LITTLE, 0x0102);
        CodecUtils.uint16Type(datagram, 2 - datagram.length, ByteOrder.BIG, 0x0304);

        byte[] cache = new byte[]{0x02, 0x01, 0x03, 0x04};

        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testDecodeUInt32() {
        byte[] datagram = new byte[10];

        datagram[0] = 1;
        datagram[1] = 0;
        datagram[2] = 0;
        datagram[3] = 2;

        assertEquals(CodecUtils.uint32Type(datagram, 0, ByteOrder.LITTLE), 1 + 2 * 256L * 256 * 256);
        assertEquals(CodecUtils.uint32Type(datagram, 0, ByteOrder.BIG), 256 * 256 * 256 + 2);
        assertEquals(CodecUtils.uint32Type(datagram, 0 - datagram.length, ByteOrder.BIG), 256 * 256 * 256 + 2);
    }

    @Test
    public void testEncodeUInt32() {
        byte[] datagram = new byte[8];

        CodecUtils.uint32Type(datagram, 0, ByteOrder.LITTLE, 0x01020304);
        CodecUtils.uint32Type(datagram, 4 - datagram.length, ByteOrder.BIG, 0x05060708);

        byte[] cache = new byte[]{0x04, 0x03, 0x02, 0x01, 0x05, 0x06, 0x07, 0x08};

        assertArrayEquals(datagram, cache);
    }

    public static List<Arguments> testDecodeUInt64() {
        return Stream.of(
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 0, 0}, ByteOrder.LITTLE, new BigInteger(String.valueOf((long) Math.pow(256, 4)))),
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 0, 1}, ByteOrder.LITTLE, new BigInteger(String.valueOf((long) Math.pow(256, 4) + (long) Math.pow(256, 7)))),
                Arguments.arguments(new byte[]{0, 0, 0, 0, 1, 0, 1, 1}, ByteOrder.BIG, new BigInteger(String.valueOf((long) Math.pow(256, 3) + 1 + 256)))
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testDecodeUInt64(byte[] datagram, ByteOrder policy, BigInteger value) {
        assertEquals(CodecUtils.uint64Type(datagram, 0, policy), value);
        assertEquals(CodecUtils.uint64Type(datagram, -8, policy), value);
    }

    public static List<Arguments> testEncodeUInt64() {
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
    public void testEncodeUInt64(byte[] datagram, int byteOffset, ByteOrder policy, BigInteger value, byte[] expected) {
        CodecUtils.uint64Type(datagram, byteOffset, policy, value);

        assertArrayEquals(expected, datagram);
    }

    @Test
    public void testDecodeFloat() {
        float pi = 3.141f;
        float e = 2.718f;

        assertEquals(CodecUtils.floatType(BinaryUtils.valueOf(pi), 0, ByteOrder.LITTLE), pi, 0.0001);
        assertEquals(CodecUtils.floatType(BinaryUtils.valueOf(e), 0, ByteOrder.LITTLE), e, 0.0001);

        assertEquals(CodecUtils.floatType(BinaryUtils.valueOf(e, ByteOrder.BIG), 0, ByteOrder.BIG), e, 0.0001);
        assertEquals(CodecUtils.floatType(BinaryUtils.valueOf(e, ByteOrder.BIG), -4, ByteOrder.BIG), e, 0.0001);
    }

    @Test
    public void testEncodeFloat() {
        byte[] datagram = new byte[4];
        float pi = 3.141f;
        float e = 2.718f;

        CodecUtils.floatType(datagram, 0, ByteOrder.LITTLE, pi);
        assertArrayEquals(datagram, BinaryUtils.valueOf(pi));

        CodecUtils.floatType(datagram, 0, ByteOrder.LITTLE, e);
        assertArrayEquals(datagram, BinaryUtils.valueOf(e));

        CodecUtils.floatType(datagram, 0 - datagram.length, ByteOrder.BIG, e);
        assertArrayEquals(datagram, BinaryUtils.valueOf(e, ByteOrder.BIG));
    }

    @Test
    public void testDecodeDouble() {
        double pi = 3.141;
        double e = 2.718;

        assertEquals(CodecUtils.doubleType(BinaryUtils.valueOf(pi), 0, ByteOrder.LITTLE), pi, 0.001);
        assertEquals(CodecUtils.doubleType(BinaryUtils.valueOf(e), 0, ByteOrder.LITTLE), e, 0.001);

        assertEquals(CodecUtils.doubleType(BinaryUtils.valueOf(pi, ByteOrder.BIG), 0, ByteOrder.BIG), pi, 0.001);
        assertEquals(CodecUtils.doubleType(BinaryUtils.valueOf(pi, ByteOrder.BIG), -8, ByteOrder.BIG), pi, 0.001);
    }
    @Test
    public void testEncodeDouble() {
        byte[] datagram = new byte[8];
        double pi = 3.141;
        double e = 2.718;

        CodecUtils.doubleType(datagram, 0, ByteOrder.LITTLE, e);
        assertArrayEquals(datagram, BinaryUtils.valueOf(e));

        CodecUtils.doubleType(datagram, 0, ByteOrder.LITTLE, pi);
        assertArrayEquals(datagram, BinaryUtils.valueOf(pi));

        CodecUtils.doubleType(datagram, 0 - datagram.length, ByteOrder.BIG, pi);
        assertArrayEquals(datagram, BinaryUtils.valueOf(pi, ByteOrder.BIG));
    }
}
