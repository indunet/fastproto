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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import org.indunet.fastproto.annotation.EnumType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of enum type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class EnumCodecTest {
    EnumCodec codec = new EnumCodec();

    @Test
    public void testDecode1() {
        val datagram = new byte[10];

        datagram[0] = 0;
        datagram[1] = 8;
        datagram[2] = 9;

        assertEquals(Color.GREEN, codec.decode(datagram, 0, "", Color.class));
        assertEquals(Color.RED, codec.decode(datagram, 1, "code", Color.class));
        assertEquals(Color.YELLOW, codec.decode(datagram, 2, "code", Color.class));
    }

    @Test
    public void testDecode2() {
        val datagram = new byte[10];

        datagram[0] = 100;

        assertThrows(DecodingException.class, () -> codec.decode(datagram, 0, "number", Color.class));
        assertThrows(DecodingException.class, () -> codec.decode(datagram, 0, "", Color.class));
        assertThrows(DecodingException.class, () -> codec.decode(datagram, 0, "", Color.class));
    }

    @Test
    public void testEncode1() {
        val datagram = new byte[10];
        val expected = new byte[10];

        expected[0] = 0;
        expected[1] = 8;
        expected[2] = 9;

        this.codec.encode(datagram, 0, "", Color.GREEN);
        this.codec.encode(datagram, 1, "code", Color.RED);
        this.codec.encode(datagram, 2, "code", Color.YELLOW);

        assertArrayEquals(expected, datagram);
    }

    @Test
    public void testEncode2() {
        val datagram = new byte[10];

        datagram[0] = 100;

        assertThrows(EncodingException.class, () -> codec.encode(datagram, 0, "number", Color.RED));
    }

    @AllArgsConstructor
    public enum Color {
        GREEN(0x01),
        RED(0x08),
        YELLOW(0x09);

        int code;
    }

    @Data
    public static class EnumObject {
        @EnumType(offset = 0)
        Color color1;

        @EnumType(offset = 1, name = "code")
        Color color2;
    }
}
