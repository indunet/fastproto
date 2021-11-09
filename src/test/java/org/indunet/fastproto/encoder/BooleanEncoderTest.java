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

package org.indunet.fastproto.encoder;


import org.indunet.fastproto.exception.EncodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class BooleanEncoderTest {
    BooleanEncoder encoder = new BooleanEncoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 0, 0, true);
        encoder.encode(datagram, 1, 7, true);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);

        encoder.encode(datagram, 0, 0, false);
        encoder.encode(datagram, 1 - datagram.length, 7, false);

        assertEquals(datagram[0], 0);
        assertEquals(datagram[1], 0);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        // assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, -1, true));

        assertThrows(EncodingException.class,
                () -> this.encoder.encode(datagram, -101, 1, true));
        assertThrows(EncodingException.class,
                () -> this.encoder.encode(datagram, 10, 1, true));
    }
}
