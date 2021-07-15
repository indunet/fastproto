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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Deng Ran
 * @since 2.0.0
 */
public class EncodeUtilsTest {
    @Test
    public void testBooleanType() {
        byte[] datagram = new byte[10];

        EncodeUtils.type(datagram, 0, 0, true);
        EncodeUtils.type(datagram, 1, 7, true);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);
    }

    @Test
    public void testBinaryType() {
        val datagram = new byte[4];

        EncodeUtils.type(datagram, 0, -1, new byte[]{1, 2, 3, 4});
        assertArrayEquals(datagram, new byte[]{1, 2, 3, 4});
    }

    @Test
    public void testShortType() {
        val datagram = new byte[4];

        EncodeUtils.type(datagram, 0, EndianPolicy.BIG, (short) 0x0102);
        EncodeUtils.type(datagram, 2 - datagram.length, EndianPolicy.LITTLE, (short) 0x0304);

        val cache = new byte[]{1, 2, 4, 3};
        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testInteger() {
        val datagram = new byte[4];

        EncodeUtils.type(datagram, 0, EndianPolicy.LITTLE, -101);

        assertArrayEquals(BinaryUtils.valueOf(-101), datagram);
    }

    @Test
    public void testLongType() {
        val datagram = new byte[8];

        EncodeUtils.type(datagram, 0, -101L);

        assertArrayEquals(BinaryUtils.valueOf(-101L), datagram);
    }

    @Test
    public void testFloatType() {
        byte[] datagram = new byte[4];
        float pi = 3.141f;
        float e = 2.718f;

        EncodeUtils.type(datagram, 0, EndianPolicy.LITTLE, pi);
        assertArrayEquals(datagram, BinaryUtils.valueOf(pi));

        EncodeUtils.type(datagram, 0, EndianPolicy.LITTLE, e);
        assertArrayEquals(datagram, BinaryUtils.valueOf(e));

        EncodeUtils.type(datagram, 0 - datagram.length, EndianPolicy.BIG, e);
        assertArrayEquals(datagram, BinaryUtils.valueOf(e, EndianPolicy.BIG));
    }

    @Test
    public void testDoubleType() {
        byte[] datagram = new byte[8];
        double pi = 3.141;
        double e = 2.718;

        EncodeUtils.type(datagram, 0, EndianPolicy.LITTLE, e);
        assertArrayEquals(datagram, BinaryUtils.valueOf(e));

        EncodeUtils.type(datagram, 0, EndianPolicy.LITTLE, pi);
        assertArrayEquals(datagram, BinaryUtils.valueOf(pi));

        EncodeUtils.type(datagram, 0 - datagram.length, EndianPolicy.BIG, pi);
        assertArrayEquals(datagram, BinaryUtils.valueOf(pi, EndianPolicy.BIG));
    }

    @Test
    public void testStringType() {
        val datagram = new byte[6];

        EncodeUtils.type(datagram, 0, -1, Charset.defaultCharset(), "ABCabc");
        assertArrayEquals(datagram, "ABCabc".getBytes(StandardCharsets.UTF_8));
    }
}
