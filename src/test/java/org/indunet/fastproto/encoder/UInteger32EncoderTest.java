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
 * @since 1.2.1
 */
public class UInteger32EncoderTest {
    UInteger32Encoder encoder = new UInteger32Encoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[8];

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, 0x01020304);
        encoder.encode(datagram, 4 - datagram.length, EndianPolicy.BIG, 0x05060708);

        byte[] cache = new byte[]{0x04, 0x03, 0x02, 0x01, 0x05, 0x06, 0x07, 0x08};
        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(EncodeException.class,
                () -> encoder.encode(datagram, 10, EndianPolicy.LITTLE, 1));
        assertThrows(EncodeException.class,
                () -> encoder.encode(datagram, 0, EndianPolicy.LITTLE, Long.MAX_VALUE));
    }
}