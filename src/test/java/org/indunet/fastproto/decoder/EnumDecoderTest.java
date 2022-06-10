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
package org.indunet.fastproto.decoder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.EnumType;
import org.indunet.fastproto.exception.DecodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Double type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 2.1.0
 */
public class EnumDecoderTest {
    EnumDecoder decoder = new EnumDecoder();

    @AllArgsConstructor
    public static enum Color {
        GREEN(0x01),
        RED(0x08),
        YELLOW(0x09);

        int code;
    }

    @Data
    public static class EnumObject {
        @EnumType(0)
        Color color1;

        @EnumType(value = 1, field = "code")
        Color color2;
    }

    @Test
    public void testDecode1() {
        val datagram = new byte[10];

        datagram[0] = 0;
        datagram[3] = 8;
        datagram[7] = 9;

        assertEquals(Color.GREEN, decoder.decode(datagram, 0, EndianPolicy.LITTLE,
                ProtocolType.UINT8, "", Color.class));
        assertEquals(Color.RED, decoder.decode(datagram, 2, EndianPolicy.BIG,
                ProtocolType.UINT16, "code", Color.class));
        assertEquals(Color.YELLOW, decoder.decode(datagram, 4, EndianPolicy.BIG,
                ProtocolType.INT32, "code", Color.class));
    }

    @Test
    public void testDecode2() {
        val datagram = new byte[10];

        datagram[0] = 100;

        assertThrows(DecodingException.class, () -> {
            decoder.decode(datagram, 0, EndianPolicy.LITTLE,
                    ProtocolType.UINT8, "number", Color.class);
        });
        assertThrows(DecodingException.class, () -> {
            decoder.decode(datagram, 0, EndianPolicy.LITTLE,
                    ProtocolType.UINT32, "", Color.class);
        });
        assertThrows(DecodingException.class, () -> {
            decoder.decode(datagram, 0, EndianPolicy.LITTLE,
                    ProtocolType.UINT8, "", Color.class);
        });
    }
}
