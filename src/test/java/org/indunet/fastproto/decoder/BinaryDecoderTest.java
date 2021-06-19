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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BinaryDecoderTest {
    BinaryDecoder decoder = new BinaryDecoder();

    @Test
    public void testDecode1() {
        byte[] datagram = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertArrayEquals(decoder.decode(datagram, 0, 2), new byte[]{0, 1});
        assertArrayEquals(decoder.decode(datagram, 3, 4), new byte[]{3, 4, 5, 6});
        assertArrayEquals(decoder.decode(datagram, 5, 5), new byte[]{5, 6, 7, 8, 9});
        assertArrayEquals(decoder.decode(datagram, 6, -1), new byte[]{6, 7, 8, 9});
        assertArrayEquals(decoder.decode(datagram, -4, -1), new byte[]{6, 7, 8, 9});
        assertArrayEquals(decoder.decode(datagram, -4, -2), new byte[]{6, 7, 8});

    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 2, 10));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 2, 10));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -2, 10));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 2, -10));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, -1));
    }
}
