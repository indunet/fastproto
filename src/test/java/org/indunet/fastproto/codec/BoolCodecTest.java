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

import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of bool type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class BoolCodecTest {
    BoolCodec codec = new BoolCodec();

    @Test
    public void testDecode1() {
        byte[] datagram = {0x01, 0x02, 0x04, 0x08};

        assertTrue(codec.decode(datagram, 0, 0));
        assertFalse(codec.decode(datagram, 0, 1));

        assertTrue(codec.decode(datagram, 1, 1));
        assertTrue(codec.decode(datagram, 2, 2));
        assertTrue(codec.decode(datagram, 3, 3));
        assertTrue(codec.decode(datagram, -1, 3));
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 2, 10));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, -101, 0));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 0, -1));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10, 0));
    }

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[10];

        this.codec.encode(datagram, 0, 0, true);
        codec.encode(datagram, 1, 7, true);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);

        this.codec.encode(datagram, 0, 0, false);
        this.codec.encode(datagram, 1 - datagram.length, 7, false);

        assertEquals(datagram[0], 0);
        assertEquals(datagram[1], 0);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        // assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, -1, true));

        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, -101, 1, true));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 10, 1, true));
    }
}
