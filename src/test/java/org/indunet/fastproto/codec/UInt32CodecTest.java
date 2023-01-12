/*
 * Copyright 2019-2022 indunet.org
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

package org.indunet.fastproto.codec;

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of uint32 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class UInt32CodecTest {
    UInt32Codec codec = new UInt32Codec();

    @Test
    public void testDecode1() {
        byte[] datagram = new byte[10];
        datagram[0] = 1;
        datagram[1] = 0;
        datagram[2] = 0;
        datagram[3] = 2;

        assertEquals(this.codec.decode(datagram, 0, ByteOrder.LITTLE), 1 + 2 * 256L * 256 * 256);
        assertEquals(this.codec.decode(datagram, 0, ByteOrder.BIG), 256 * 256 * 256 + 2);
        assertEquals(this.codec.decode(datagram, 0 - datagram.length, ByteOrder.BIG), 256 * 256 * 256 + 2);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 0, ByteOrder.LITTLE));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, -1, ByteOrder.LITTLE));
        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 8, ByteOrder.LITTLE));
    }

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[8];

        this.codec.encode(datagram, 0, ByteOrder.LITTLE, 0x01020304);
        this.codec.encode(datagram, 4 - datagram.length, ByteOrder.BIG, 0x05060708);

        byte[] cache = new byte[]{0x04, 0x03, 0x02, 0x01, 0x05, 0x06, 0x07, 0x08};
        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 10, ByteOrder.LITTLE, 1));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 0, ByteOrder.LITTLE, Long.MAX_VALUE));
    }
}