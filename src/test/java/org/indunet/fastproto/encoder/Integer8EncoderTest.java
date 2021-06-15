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

import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class Integer8EncoderTest {
    Integer8Encoder encoder = new Integer8Encoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 0, 10);
        encoder.encode(datagram, 1 - datagram.length, -52);

        assertEquals(datagram[0], 10);
        assertEquals(datagram[1], -52);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, 8));

        assertThrows(EncodeException.class, () -> encoder.encode(datagram, -101, 1));
        assertThrows(EncodeException.class, () -> encoder.encode(datagram, 10, 255));
    }
}