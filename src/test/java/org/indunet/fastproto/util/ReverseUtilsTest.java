/*
 * Copyright 2019-2023 indunet.org
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


package org.indunet.fastproto.util;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of reverse utils.
 *
 * @author Deng Ran
 * @since 3.10.2
 */
public class ReverseUtilsTest {
    @Test
    public void testReverseOffset() {
        val bytes = new byte[10];

        assertEquals(9, ReverseUtils.reverse(bytes, -1));
        assertEquals(8, ReverseUtils.reverse(bytes, -2));
    }

    @Test
    public void testReverseLength() {
        val bytes = new byte[10];

        assertEquals(10, ReverseUtils.reverse(bytes, 0, 10));
        assertEquals(9, ReverseUtils.reverse(bytes, 0, 9));
        assertEquals(10, ReverseUtils.reverse(bytes, 0, -1));
        assertEquals(9, ReverseUtils.reverse(bytes, 0, -2));
    }
}
