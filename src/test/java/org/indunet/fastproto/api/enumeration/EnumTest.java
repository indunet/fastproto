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

package org.indunet.fastproto.api.enumeration;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * String object.
 *
 * @author Deng Ran
 * @since 3.4.0
 */
public class EnumTest {
    @Test
    public void testParse() {
        byte[] bytes = new byte[4];
        val expected = new EnumObject();

        bytes[0] = (byte) expected.red.ordinal();
        bytes[1] =  (byte) expected.yellow.ordinal();
        bytes[2] = (byte) expected.green.getCode();

        assertEquals(FastProto.parse(bytes, EnumObject.class), expected);
    }

    @Test
    public void testToBytes() {
        byte[] expected = new byte[4];
        val object = new EnumObject();

        expected[0] = (byte) object.red.ordinal();
        expected[1] =  (byte) object.yellow.ordinal();
        expected[2] = (byte) object.green.getCode();

        assertArrayEquals(FastProto.toBytes(object, 4), expected);
    }
}
