/*
 * Copyright 2019-2021 indunet.org
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

import lombok.AllArgsConstructor;
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.exception.EncodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 2.0.0
 */
public class EnumEncoderTest {
    EnumEncoder encoder = new EnumEncoder();

    @AllArgsConstructor
    public static enum Color {
        GREEN(0x01),
        RED(0x08),
        YELLOW(0x09);

        int code;
    }

    @Test
    public void testEncode1() {
        val datagram = new byte[10];
        val expected = new byte[10];

        expected[0] = 0;
        expected[3] = 8;
        expected[7] = 9;

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, ProtocolType.UINT8, "", Color.GREEN);
        encoder.encode(datagram, 2, EndianPolicy.BIG, ProtocolType.UINT16, "code", Color.RED);
        encoder.encode(datagram, 4, EndianPolicy.BIG, ProtocolType.INT32, "code", Color.YELLOW);

        assertArrayEquals(expected, datagram);
    }

    @Test
    public void testEncode2() {
        val datagram = new byte[10];

        datagram[0] = 100;

        assertThrows(EncodingException.class, () -> {
            encoder.encode(datagram, 0, EndianPolicy.LITTLE,
                    ProtocolType.UINT8, "number", Color.RED);
        });
        assertThrows(EncodingException.class, () -> {
            encoder.encode(datagram, 0, EndianPolicy.LITTLE,
                    ProtocolType.UINT32, "", Color.GREEN);
        });
    }
}
