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

import lombok.val;
import org.indunet.fastproto.ByteOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test of char array type codec.
 *
 * @author Deng Ran
 * @since 3.9.1
 */
public class CharArrayCodecTest {
    CharArrayCodec codec = new CharArrayCodec();

    @Test
    public void testDecode() {
        val expected = new char[] {'中', '文'};
        val bytes = new byte[] {'中' & 0xFF, '中' >>> 8 & 0xFF, (byte) ('文' & 0xFF), '文' >>> 8 & 0xFF};

        assertArrayEquals(expected, codec.decode(bytes, 0, 2, ByteOrder.LITTLE));
    }

    @Test
    public void testEncode() {
        val chars = new char[] {'中', '文'};
        val expected = new byte[] {'中' & 0xFF, '中' >>> 8 & 0xFF, (byte) ('文' & 0xFF), '文' >>> 8 & 0xFF};
        val actual = new byte[expected.length];

        this.codec.encode(actual, 0, 2, ByteOrder.LITTLE, chars);
        assertArrayEquals(expected, actual);
    }
}
