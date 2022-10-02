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

package org.indunet.fastproto.api.endian;

import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.exception.CodecException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @since 1.4.0
 */
class EndianTest {
    @Test
    public void testParse() {
        val expected = new EndianObject();
        val bytes = new byte[4];

        bytes[0] = 0x02;
        bytes[1] = 0x01;
        bytes[2] = 0x03;
        bytes[3] = 0x04;

        assertEquals(FastProto.parse(bytes, EndianObject.class), expected);
    }

    @Test
    public void testToBytes() {
        val object = new EndianObject();
        val expected = new byte[4];

        expected[0] = 0x02;
        expected[1] = 0x01;
        expected[2] = 0x03;
        expected[3] = 0x04;

        assertArrayEquals(FastProto.toBytes(object, 4), expected);
    }

    @Test
    void testByName() {
        Assertions.assertEquals(EndianPolicy.byName("Big"), EndianPolicy.BIG);
        assertEquals(EndianPolicy.byName("Little"), EndianPolicy.LITTLE);

        assertThrows(CodecException.class, () -> EndianPolicy.byName("Unknown"));
    }
}