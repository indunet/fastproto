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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test of byte type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class ByteCodecTest {
    ByteCodec codec = new ByteCodec();

    @Test
    public void testDecode1() {
        byte[] datagram = {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(codec.decode(datagram, 0), -2);
        assertEquals(codec.decode(datagram, 1), -1);
        assertEquals(codec.decode(datagram, 2), 0);
        assertEquals(codec.decode(datagram, 4), 2);
        assertEquals(codec.decode(datagram, 10), 8);
        assertEquals(codec.decode(datagram, 11), 9);
        assertEquals(codec.decode(datagram, 11 - datagram.length), 9);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 2));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, -101));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10));
    }

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[10];

        this.codec.encode(datagram, 0, (byte) 1);
        this.codec.encode(datagram, 1 - datagram.length, (byte) -128);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.encode(null, 0, (byte) 1));

        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, -101, (byte) 1));
        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, 10, (byte) 1));
    }
}
