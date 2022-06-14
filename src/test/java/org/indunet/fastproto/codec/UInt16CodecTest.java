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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of uint16 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class UInt16CodecTest {
    UInt16Codec codec = new UInt16Codec();

    @Test
    public void testDecode1() {
        byte[] datagram = new byte[10];
        datagram[0] = 1;
        datagram[1] = 2;

        assertEquals(this.codec.decode(datagram, 0, EndianPolicy.LITTLE), 1 + 2 * 256);
        assertEquals(this.codec.decode(datagram, 0, EndianPolicy.BIG), 256 + 2);

        assertEquals(this.codec.decode(datagram, -10, EndianPolicy.BIG), 256 + 2);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 0, EndianPolicy.LITTLE));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, -11, EndianPolicy.LITTLE));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10, EndianPolicy.LITTLE));
    }

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[4];

        this.codec.encode(datagram, 0, EndianPolicy.LITTLE, 0x0102);
        this.codec.encode(datagram, 2 - datagram.length, EndianPolicy.BIG, 0x0304);

        byte[] cache = new byte[]{0x02, 0x01, 0x03, 0x04};
        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 10, EndianPolicy.LITTLE, 1));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 10, EndianPolicy.LITTLE, Integer.MAX_VALUE));
    }
}