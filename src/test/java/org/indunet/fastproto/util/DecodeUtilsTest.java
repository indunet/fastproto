/*
 * Copyright 2019-2021 indunet
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
import org.indunet.fastproto.EndianPolicy;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @version 2.0.0
 */
public class DecodeUtilsTest {
    @Test
    public void testBinaryType() {
        byte[] datagram = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertArrayEquals(DecodeUtils.binaryType(datagram, 0, 3),
                new byte[] {0, 1, 2});
    }

    @Test
    public void testCharacterType() {
        byte[] datagram = new byte[10];
        datagram[0] = 'a';
        datagram[2] = 'A';

        assertEquals('a', (char) DecodeUtils.characterType(datagram, 0));
        assertEquals('A', (char) DecodeUtils.characterType(datagram, 2, EndianPolicy.LITTLE));
    }

    @Test
    public void testBooleanType() {
        byte[] datagram = {0x01, 0x02, 0x04, 0x08};

        assertTrue(DecodeUtils.booleanType(datagram, 0, 0));
        assertFalse(DecodeUtils.booleanType(datagram, 0, 1));
    }

    @Test
    public void testByteType() {
        byte[] datagram = {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(DecodeUtils.byteType(datagram, 0), -2);
        assertEquals(DecodeUtils.byteType(datagram, 1), -1);
    }

    @Test
    public void testInteger8Type() {
        byte[] datagram = new byte[10];
        datagram[0] = -10;
        datagram[1] = 29;

        assertEquals(DecodeUtils.integer8Type(datagram, 0), -10);
        assertEquals(DecodeUtils.integer8Type(datagram, 1), 29);
        assertEquals(DecodeUtils.integer8Type(datagram, 1 - datagram.length), 29);
    }

    @Test
    public void testInteger16Type() {
        byte[] datagram = new byte[10];

        // Value 1
        datagram[0] = 1;
        datagram[1] = 2;

        // Value 2
        short value = -29;
        datagram[2] |= (value >> 8);
        datagram[3] |= (value & 0xFF);

        assertEquals(DecodeUtils.integer16Type(datagram, 0), 1 + 256 * 2);
        assertEquals(DecodeUtils.integer16Type(datagram, 0, EndianPolicy.BIG), 256 + 2);
        assertEquals(DecodeUtils.integer16Type(datagram, 2, EndianPolicy.BIG), -29);

        assertEquals(DecodeUtils.integer16Type(datagram, -8, EndianPolicy.BIG), -29);
    }

    @Test
    public void testShortType() {
        byte[] datagram = {0, 1, 2, 3, -1, -1, -2, -1};

        // For little endian.
        assertEquals(DecodeUtils.shortType(datagram, 0), 256);
        assertEquals(DecodeUtils.shortType(datagram, 2, EndianPolicy.LITTLE), 2 + 3 * 256);
        assertEquals(DecodeUtils.shortType(datagram, 4, EndianPolicy.LITTLE), -1);
        assertEquals(DecodeUtils.shortType(datagram, 6, EndianPolicy.LITTLE), -2);

        // For big endian.
        assertEquals(DecodeUtils.shortType(datagram, 0, EndianPolicy.BIG), 0x0001);
        assertEquals(DecodeUtils.shortType(datagram, 2, EndianPolicy.BIG), 0x0203);
        assertEquals(DecodeUtils.shortType(datagram, 2 - datagram.length, EndianPolicy.BIG), 0x0203);

    }

    @Test
    public void testUInteger32Type() {
        byte[] datagram = new byte[10];
        datagram[0] = 1;
        datagram[1] = 0;
        datagram[2] = 0;
        datagram[3] = 2;

        assertEquals(DecodeUtils.uInteger32Type(datagram, 0), 1 + 2 * 256L * 256 * 256);
        assertEquals(DecodeUtils.uInteger32Type(datagram, 0, EndianPolicy.BIG), 256 * 256 * 256 + 2);
        assertEquals(DecodeUtils.uInteger32Type(datagram, 0 - datagram.length, EndianPolicy.BIG), 256 * 256 * 256 + 2);
    }

    @Test
    public void testUInteger16() {
        byte[] datagram = new byte[10];

        datagram[0] = 1;
        datagram[1] = 2;

        assertEquals(DecodeUtils.uInteger16Type(datagram, 0), 1 + 2 * 256);
        assertEquals(DecodeUtils.uInteger16Type(datagram, 0, EndianPolicy.BIG), 256 + 2);
        assertEquals(DecodeUtils.uInteger16Type(datagram, -10, EndianPolicy.BIG), 256 + 2);
    }

    @Test
    public void testIntegerType() {
        byte[] datagram = {0, 1, 0, 1, -1, -1, -1, -1};

        assertEquals(DecodeUtils.integerType(datagram, 0), 256 + 256 * 256 * 256);
        assertEquals(DecodeUtils.integerType(datagram, 4, EndianPolicy.LITTLE), -1);

        assertEquals(DecodeUtils.integerType(datagram, 0, EndianPolicy.BIG), 0x00010001);
        assertEquals(DecodeUtils.integerType(datagram, 4, EndianPolicy.BIG), -1);
        assertEquals(DecodeUtils.integerType(datagram, 4 - datagram.length, EndianPolicy.BIG), -1);
    }

    @Test
    public void testLoneType() {
        val datagram = new byte[] {-1, -1, -1, -1, -1, -1, -1, -1};

        assertEquals(DecodeUtils.longType(datagram, 0, EndianPolicy.LITTLE), -1);
    }

    @Test
    public void testFloatType() {
        float pi = 3.141f;
        float e = 2.718f;

        assertEquals(DecodeUtils.floatType(BinaryUtils.valueOf(pi), 0), pi, 0.0001);
        assertEquals(DecodeUtils.floatType(BinaryUtils.valueOf(e), 0, EndianPolicy.LITTLE), e, 0.0001);

        assertEquals(DecodeUtils.floatType(BinaryUtils.valueOf(e, EndianPolicy.BIG), 0, EndianPolicy.BIG), e, 0.0001);
        assertEquals(DecodeUtils.floatType(BinaryUtils.valueOf(e, EndianPolicy.BIG), -4, EndianPolicy.BIG), e, 0.0001);

    }

    @Test
    public void testDoubleType() {
        double pi = 3.141;
        double e = 2.718;

        assertEquals(DecodeUtils.doubleType(BinaryUtils.valueOf(pi), 0), pi, 0.001);
        assertEquals(DecodeUtils.doubleType(BinaryUtils.valueOf(e), 0, EndianPolicy.LITTLE), e, 0.001);

        assertEquals(DecodeUtils.doubleType(BinaryUtils.valueOf(pi, EndianPolicy.BIG), 0, EndianPolicy.BIG), pi, 0.001);
        assertEquals(DecodeUtils.doubleType(BinaryUtils.valueOf(pi, EndianPolicy.BIG), -8, EndianPolicy.BIG), pi, 0.001);
    }

    @Test
    public void testStringType() {
        val datagram = "ABCabc".getBytes();

        assertEquals(DecodeUtils.stringType(datagram, 0, -1, StandardCharsets.UTF_8), "ABCabc");
    }

    @Test
    public void testUInteger64Type() {
        val datagram = new byte[] {0, 0, 0, 0, 1, 0, 0, 0};

        assertEquals(DecodeUtils.uInteger64Type(datagram, 0, EndianPolicy.LITTLE),
                new BigInteger(String.valueOf((long) Math.pow(256, 4))));
    }
}
