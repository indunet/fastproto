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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test of uint32 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class UInt32ArrayCodecTest {
    UInt32ArrayCodec codec = new UInt32ArrayCodec();

    @Test
    public void testDecode1() {
        val random = new Random();
        val expected = IntStream.range(0, 10)
                .mapToLong(i -> (long) abs(random.nextInt()))
                .toArray();

        val bytes = BinaryUtils.uint32Of(expected, EndianPolicy.LITTLE);

        assertArrayEquals(codec.decode(bytes, 0, 2, EndianPolicy.LITTLE), Arrays.copyOfRange(expected, 0, 2));
        assertArrayEquals(codec.decode(bytes, 12, 4, EndianPolicy.LITTLE), Arrays.copyOfRange(expected, 3, 7));
        assertArrayEquals(codec.decode(bytes, 20, 5, EndianPolicy.LITTLE), Arrays.copyOfRange(expected, 5, 10));
        assertArrayEquals(codec.decode(bytes, 24, -1, EndianPolicy.LITTLE), Arrays.copyOfRange(expected, 6, 10));
        assertArrayEquals(codec.decode(bytes, -16, -1, EndianPolicy.LITTLE), Arrays.copyOfRange(expected, 6, 10));
        assertArrayEquals(codec.decode(bytes, -16, -2, EndianPolicy.LITTLE), Arrays.copyOfRange(expected, 6, 9));
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 2, 10, EndianPolicy.LITTLE));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 2, 10, EndianPolicy.LITTLE));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, -2, 10, EndianPolicy.LITTLE));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 2, -10, EndianPolicy.LITTLE));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10, -1, EndianPolicy.LITTLE));
    }

    @Test
    public void testEncode1() {
        val random = new Random();
        val values = new long[16];

        IntStream.range(0, 16)
                .forEach(i -> values[i] = abs(random.nextInt()));

        val bytes = new byte[16 * 4];

        this.codec.encode(bytes, 0, 16, EndianPolicy.LITTLE, values);
        assertArrayEquals(bytes, BinaryUtils.uint32Of(values, EndianPolicy.LITTLE));

        this.codec.encode(bytes, 0, -1, EndianPolicy.LITTLE, values);
        assertArrayEquals(bytes, BinaryUtils.uint32Of(values, EndianPolicy.LITTLE));
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.encode(null, 0, -1, EndianPolicy.LITTLE, new long[8]));
        assertThrows(NullPointerException.class, () -> this.codec.encode(datagram, 0, -1, EndianPolicy.LITTLE, null));

        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, -2, -7, EndianPolicy.LITTLE, new long[8]));
        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, -11, -7, EndianPolicy.LITTLE, new long[8]));
        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, 10, -1, EndianPolicy.LITTLE, new long[8]));
        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, 0, 11, EndianPolicy.LITTLE, new long[8]));
    }
}
