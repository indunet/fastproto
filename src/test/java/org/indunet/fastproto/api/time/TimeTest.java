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

package org.indunet.fastproto.api.time;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Time test.
 *
 * @author Deng Ran
 * @since 3.4.0
 */
public class TimeTest {
    @Test
    public void testDecode() {
        val expected = new TimeObject();
        val bytes = expected.toBytes();

        assertEquals(expected, FastProto.decode(bytes, TimeObject.class));
    }

    @Test
    public void testEncode() {
        val obj = new TimeObject();
        val expected = obj.toBytes();

        assertArrayEquals(expected, FastProto.encode(obj, 32));
    }
}
