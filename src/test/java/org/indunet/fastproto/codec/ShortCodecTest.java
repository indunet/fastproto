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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of short type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class ShortCodecTest {
    ShortCodec codec = new ShortCodec();

    @Test
    public void testDecode1() {
        byte[] datagram = {0, 1, 2, 3, -1, -1, -2, -1};

        // For little endian.
        assertEquals(codec.decode(datagram, 0, EndianPolicy.LITTLE), 256);
        assertEquals(codec.decode(datagram, 2, EndianPolicy.LITTLE), 2 + 3 * 256);
        assertEquals(codec.decode(datagram, 4, EndianPolicy.LITTLE), -1);
        assertEquals(codec.decode(datagram, 6, EndianPolicy.LITTLE), -2);

        // For big endian.
        assertEquals(codec.decode(datagram, 0, EndianPolicy.BIG), 0x0001);
        assertEquals(codec.decode(datagram, 2, EndianPolicy.BIG), 0x0203);
        assertEquals(codec.decode(datagram, 2 - datagram.length, EndianPolicy.BIG), 0x0203);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 0, EndianPolicy.LITTLE));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, -1, EndianPolicy.LITTLE));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10, EndianPolicy.LITTLE));
    }

    @Test
    public void testEncode1() {
        val datagram = new byte[4];

        this.codec.encode(datagram, 0, EndianPolicy.BIG, (short) 0x0102);
        this.codec.encode(datagram, 2 - datagram.length, EndianPolicy.LITTLE, (short) 0x0304);

        val cache = new byte[]{1, 2, 4, 3};
        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testEncode2() {
        val datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.encode(null, 0, EndianPolicy.BIG, (short) 1));

        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, -1, EndianPolicy.BIG, (short) 0));
        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, 10, EndianPolicy.LITTLE, (short) 0));
    }
}
