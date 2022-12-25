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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of AsciiCodec
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class AsciiCodecTest {
    AsciiCodec codec = new AsciiCodec();

    @Test
    public void testDecode1() {
        byte[] datagram = new byte[10];
        datagram[0] = 'a';
        datagram[2] = 'A';

        assertEquals('a', (char) codec.decode(datagram, 0));
        assertEquals('A', (char) codec.decode(datagram, 2));
        assertEquals('A', (char) codec.decode(datagram, 2 - datagram.length));
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 10));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10));
    }

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[10];

        this.codec.encode(datagram, 0, 'A');
        this.codec.encode(datagram, 2 - datagram.length, 'a');

        byte[] cache = new byte[10];
        cache[0] = 65;
        cache[2] = 97;
        assertArrayEquals(cache, datagram);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.encode(null, 0, 'A'));
    }
}