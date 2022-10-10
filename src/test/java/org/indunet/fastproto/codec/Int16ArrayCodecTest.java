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
import org.indunet.fastproto.annotation.Int16Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test of int16 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class Int16ArrayCodecTest {
    Int16ArrayCodec codec = new Int16ArrayCodec();

    @Test
    public void testDecode1() {
        val random = new Random();
        val expected = IntStream.range(0, 10)
                .map(i -> random.nextInt(Int16Type.MAX_VALUE))
                .toArray();

        val bytes = BinaryUtils.int16Of(expected, EndianPolicy.LITTLE);

        assertArrayEquals(codec.decode(bytes, 0, 2), Arrays.copyOfRange(expected, 0, 2));
        assertArrayEquals(codec.decode(bytes, 6, 4), Arrays.copyOfRange(expected, 3, 7));
        assertArrayEquals(codec.decode(bytes, 10, 5), Arrays.copyOfRange(expected, 5, 10));
        assertArrayEquals(codec.decode(bytes, 12, -1), Arrays.copyOfRange(expected, 6, 10));
        assertArrayEquals(codec.decode(bytes, -8, -1), Arrays.copyOfRange(expected, 6, 10));
        assertArrayEquals(codec.decode(bytes, -8, -2), Arrays.copyOfRange(expected, 6, 9));
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 2, 10));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 2, 10));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, -2, 10));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 2, -10));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10, -1));
    }

    @Test
    public void testEncode1() {
        val random = new Random();
        val values = new int[16];

        IntStream.range(0, 16)
                .forEach(i -> values[i] = random.nextInt(Int16Type.MAX_VALUE));

        val bytes = new byte[16 * 2];

        this.codec.encode(bytes, 0, 16, values);
        assertArrayEquals(bytes, BinaryUtils.int16Of(values, EndianPolicy.LITTLE));

        this.codec.encode(bytes, 0, -1, values);
        assertArrayEquals(bytes, BinaryUtils.int16Of(values, EndianPolicy.LITTLE));
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.encode(null, 0, -1, new int[8]));
        assertThrows(NullPointerException.class, () -> this.codec.encode(datagram, 0, -1, null));

        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, -2, -7, new int[8]));
        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, -11, -7, new int[8]));
        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, 10, -1, new int[8]));
        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, 0, 11, new int[8]));
    }
}
