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
 * Unit test of int8 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class Int8CodecTest {
    Int8Codec codec = new Int8Codec();

    @Test
    public void testDecode1() {
        byte[] datagram = new byte[10];
        datagram[0] = -10;
        datagram[1] = 29;

        assertEquals(this.codec.decode(datagram, 0), -10);
        assertEquals(this.codec.decode(datagram, 1), 29);
        assertEquals(this.codec.decode(datagram, 1 - datagram.length), 29);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 0));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, -101));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10));
    }

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[10];

        this.codec.encode(datagram, 0, 10);
        this.codec.encode(datagram, 1 - datagram.length, -52);

        assertEquals(datagram[0], 10);
        assertEquals(datagram[1], -52);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.encode(null, 0, 8));

        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, -101, 1));
        assertThrows(EncodingException.class, () -> this.codec.encode(datagram, 10, 255));
    }
}