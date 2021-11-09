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

package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShortDecoderTest {
    ShortDecoder decoder = new ShortDecoder();

    @Test
    public void testDecode1() {
        byte[] datagram = {0, 1, 2, 3, -1, -1, -2, -1};

        // For little endian.
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE), 256);
        assertEquals(decoder.decode(datagram, 2, EndianPolicy.LITTLE), 2 + 3 * 256);
        assertEquals(decoder.decode(datagram, 4, EndianPolicy.LITTLE), -1);
        assertEquals(decoder.decode(datagram, 6, EndianPolicy.LITTLE), -2);

        // For big endian.
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.BIG), 0x0001);
        assertEquals(decoder.decode(datagram, 2, EndianPolicy.BIG), 0x0203);
        assertEquals(decoder.decode(datagram, 2 - datagram.length, EndianPolicy.BIG), 0x0203);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 0, EndianPolicy.LITTLE));

        assertThrows(DecodingException.class, () -> this.decoder.decode(datagram, -1, EndianPolicy.LITTLE));
        assertThrows(DecodingException.class, () -> this.decoder.decode(datagram, 10, EndianPolicy.LITTLE));
    }
}
