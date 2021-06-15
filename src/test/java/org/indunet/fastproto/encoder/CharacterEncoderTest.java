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
import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.1.0
 */
public class CharacterEncoderTest {
    CharacterEncoder encoder = new CharacterEncoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, 'A');
        encoder.encode(datagram, 2 - datagram.length, EndianPolicy.BIG, 'a');

        byte[] cache = new byte[10];
        cache[0] = 65;
        cache[3] = 97;
        assertArrayEquals(cache, datagram);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, null, 'A'));

        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, -1, EndianPolicy.LITTLE, 'A'));
        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 10, EndianPolicy.LITTLE, 'A'));
    }
}