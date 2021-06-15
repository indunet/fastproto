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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class UInteger8DecoderTest {
    UInteger8Decoder decoder = new UInteger8Decoder();

    @Test
    public void testDecode1() {
        byte[] datagram = new byte[10];
        datagram[0] = -10;
        datagram[1] = 29;

        assertEquals(decoder.decode(datagram, 0), 256 - 10);
        assertEquals(decoder.decode(datagram, 1), 29);
        assertEquals(decoder.decode(datagram, 1 - datagram.length), 29);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 0));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -101));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10));
    }
}
