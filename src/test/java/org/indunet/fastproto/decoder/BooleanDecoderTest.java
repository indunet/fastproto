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

import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BooleanDecoderTest {
    BooleanDecoder decoder = new BooleanDecoder();

    @Test
    public void testDecode1() {
        byte[] datagram = {0x01, 0x02, 0x04, 0x08};

        assertTrue(decoder.decode(datagram, 0, 0));
        assertFalse(decoder.decode(datagram, 0, 1));

        assertTrue(decoder.decode(datagram, 1, 1));
        assertTrue(decoder.decode(datagram, 2, 2));
        assertTrue(decoder.decode(datagram, 3, 3));
        assertTrue(decoder.decode(datagram, -1, 3));
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 2, 10));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -101, 0));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 0, -1));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, 0));
    }
}
