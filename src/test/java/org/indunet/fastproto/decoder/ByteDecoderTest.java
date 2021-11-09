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

import org.indunet.fastproto.exception.DecodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ByteDecoderTest {
    ByteDecoder decoder = new ByteDecoder();

    @Test
    public void testDecode1() {
        byte[] datagram = {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(decoder.decode(datagram, 0), -2);
        assertEquals(decoder.decode(datagram, 1), -1);
        assertEquals(decoder.decode(datagram, 2), 0);
        assertEquals(decoder.decode(datagram, 4), 2);
        assertEquals(decoder.decode(datagram, 10), 8);
        assertEquals(decoder.decode(datagram, 11), 9);
        assertEquals(decoder.decode(datagram, 11 - datagram.length), 9);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 2));

        assertThrows(DecodingException.class, () -> this.decoder.decode(datagram, -101));
        assertThrows(DecodingException.class, () -> this.decoder.decode(datagram, 10));

    }
}
