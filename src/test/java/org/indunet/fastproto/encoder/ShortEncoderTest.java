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

import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
public class ShortEncoderTest {
    ShortEncoder encoder = new ShortEncoder();

    @Test
    public void testEncode1() {
        val datagram = new byte[4];

        this.encoder.encode(datagram, 0, EndianPolicy.BIG, (short) 0x0102);
        this.encoder.encode(datagram, 2 - datagram.length, EndianPolicy.LITTLE, (short) 0x0304);

        val cache = new byte[]{1, 2, 4, 3};
        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testEncode2() {
        val datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, EndianPolicy.BIG, (short) 1));

        assertThrows(EncodingException.class, () -> this.encoder.encode(datagram, -1, EndianPolicy.BIG, (short) 0));
        assertThrows(EncodingException.class, () -> this.encoder.encode(datagram, 10, EndianPolicy.LITTLE, (short) 0));
    }
}
