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

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class Integer16EncoderTest {
    Integer16Encoder encoder = new Integer16Encoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[4];

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, 0x0102);
        encoder.encode(datagram, 2 - datagram.length, EndianPolicy.BIG, -300);

        byte[] cache = new byte[4];
        cache[0] = 2;
        cache[1] = 1;
        cache[2] |= ((short) -300 >> 8);
        cache[3] |= ((short) -300 & 0xFF);

        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, EndianPolicy.BIG, 8));

        assertThrows(EncodingException.class,
                () -> this.encoder.encode(datagram, -1, EndianPolicy.LITTLE, 1));
        assertThrows(EncodingException.class,
                () -> this.encoder.encode(datagram, 10, EndianPolicy.LITTLE, 1));
        assertThrows(EncodingException.class,
                () -> this.encoder.encode(datagram, 10, EndianPolicy.LITTLE, Integer.MAX_VALUE));
    }
}