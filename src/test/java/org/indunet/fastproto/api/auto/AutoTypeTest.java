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

package org.indunet.fastproto.api.auto;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of auto type.
 *
 * @author Deng Ran
 * @since 3.7.1
 */
public class AutoTypeTest {
    @Test
    public void testParse() {
        val expected = new AutoTypeObject();
        val bytes = expected.toBytes();

        assertEquals(expected, FastProto.parse(bytes, AutoTypeObject.class));
    }

    @Test
    public void testToBytes() {
        val object = new AutoTypeObject();
        val expected = object.toBytes();

        assertArrayEquals(expected, FastProto.toBytes(object, expected.length));
    }
}
