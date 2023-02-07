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

package org.indunet.fastproto.api.string;

import lombok.val;
import lombok.var;
import org.indunet.fastproto.FastProto;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * String test.
 *
 * @author Deng Ran
 * @since 3.4.0
 */
public class StringTest {
    @Test
    public void testDecode() {
        val bytes = new byte[48];
        val expected = new StringObject();

        var buffer = expected.getString().getBytes(StandardCharsets.UTF_8);
        System.arraycopy(buffer, 0, bytes, 0, buffer.length);

        buffer = expected.getStringBuilder()
                .toString()
                .getBytes(StandardCharsets.UTF_8);
        System.arraycopy(buffer, 0, bytes, 16, buffer.length);

        buffer = expected.getStringBuffer()
                .toString()
                .getBytes(StandardCharsets.UTF_8);
        System.arraycopy(buffer, 0, bytes, 32, buffer.length);

        assertEquals(expected.toString(), FastProto.decode(bytes, StringObject.class).toString());
    }

    @Test
    public void testEncode() {
        val expected = new byte[48];
        val obj = new StringObject();

        var buffer = obj.getString().getBytes(StandardCharsets.UTF_8);
        System.arraycopy(buffer, 0, expected, 0, buffer.length);

        buffer = obj.getStringBuilder()
                .toString()
                .getBytes(StandardCharsets.UTF_8);
        System.arraycopy(buffer, 0, expected, 16, buffer.length);

        buffer = obj.getStringBuffer()
                .toString()
                .getBytes(StandardCharsets.UTF_8);
        System.arraycopy(buffer, 0, expected, 32, buffer.length);

        assertArrayEquals(expected, FastProto.encode(obj, 48));
    }
}
