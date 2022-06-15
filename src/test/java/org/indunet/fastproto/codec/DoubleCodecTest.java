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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of double type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class DoubleCodecTest {
    DoubleCodec codec = new DoubleCodec();
    double pi = 3.141;
    double e = 2.718;

    @Test
    public void testDecode1() {
        assertEquals(this.codec.decode(BinaryUtils.valueOf(pi), 0, EndianPolicy.LITTLE), pi, 0.001);
        assertEquals(this.codec.decode(BinaryUtils.valueOf(e), 0, EndianPolicy.LITTLE), e, 0.001);

        assertEquals(this.codec.decode(BinaryUtils.valueOf(pi, EndianPolicy.BIG), 0, EndianPolicy.BIG), pi, 0.001);
        assertEquals(this.codec.decode(BinaryUtils.valueOf(pi, EndianPolicy.BIG), -8, EndianPolicy.BIG), pi, 0.001);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.decode(null, 10, EndianPolicy.LITTLE));

        assertThrows(DecodingException.class,
                () -> this.codec.decode(datagram, -1, EndianPolicy.LITTLE));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(datagram, 8, EndianPolicy.LITTLE));
    }

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[8];
        double pi = 3.141;
        double e = 2.718;

        this.codec.encode(datagram, 0, EndianPolicy.LITTLE, e);
        assertArrayEquals(datagram, BinaryUtils.valueOf(e));

        this.codec.encode(datagram, 0, EndianPolicy.LITTLE, pi);
        assertArrayEquals(datagram, BinaryUtils.valueOf(pi));

        this.codec.encode(datagram, 0 - datagram.length, EndianPolicy.BIG, pi);
        assertArrayEquals(datagram, BinaryUtils.valueOf(pi, EndianPolicy.BIG));
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.encode(null, 0, null, 3.14));

        assertThrows(EncodingException.class, () ->
                this.codec.encode(datagram, -1, EndianPolicy.LITTLE, 3.141));
        assertThrows(EncodingException.class, () ->
                this.codec.encode(datagram, 10, EndianPolicy.LITTLE, 3.141));
    }
}