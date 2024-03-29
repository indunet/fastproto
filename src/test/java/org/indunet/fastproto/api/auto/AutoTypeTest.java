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
import org.indunet.fastproto.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of auto type.
 *
 * @author Deng Ran
 * @since 3.7.1
 */
public class AutoTypeTest {
    @Test
    public void testDecode() {
        val expected = new TestObject1();
        val bytes = expected.toBytes();

        assertEquals(expected, FastProto.decode(bytes, TestObject1.class));
    }

    @Test
    public void testEncode() {
        val object = new TestObject1();
        val expected = object.toBytes();

        assertArrayEquals(expected, FastProto.encode(object, expected.length));
    }

    @Test
    public void testException() {
        val object = new TestObject2();
        val bytes = new byte[10];

        assertThrows(ResolvingException.class, () -> FastProto.decode(bytes, TestObject2.class));
        assertThrows(ResolvingException.class, () -> FastProto.encode(object, 10));
    }
}
