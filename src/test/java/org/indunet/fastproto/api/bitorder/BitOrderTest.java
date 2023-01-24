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

package org.indunet.fastproto.api.bitorder;

import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.exception.CodecException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of bit order.
 *
 * @author Deng Ran
 * @since 3.9.2
 */
class BitOrderTest {
    @Test
    public void testParse() {
        val expected = new TestObject();
        val bytes = expected.toBytes();

        assertEquals(FastProto.parse(bytes, TestObject.class), expected);
    }

    @Test
    public void testToBytes() {
        val object = new TestObject();
        val expected = object.toBytes();

        assertArrayEquals(expected, FastProto.toBytes(object, 2));
    }

    @Test
    void testByName() {
        assertEquals(BitOrder.byName("MSB_0"), BitOrder.MSB_0);
        assertEquals(BitOrder.byName("LSB_0"), BitOrder.LSB_0);

        assertThrows(CodecException.class, () -> BitOrder.byName("Unknown"));
    }
}