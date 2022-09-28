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
import lombok.var;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.util.BinaryUtils;
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
    public void testParse() {
        val expected = new TimeObject();
        val bytes = new byte[32];

        var buffer = BinaryUtils.valueOf(expected.getDate().getTime());
        System.arraycopy(buffer, 0, bytes, 0, 8);

        buffer = BinaryUtils.valueOf(expected.getTimestamp().getTime());
        System.arraycopy(buffer, 0, bytes, 8, 8);

        buffer = BinaryUtils.valueOf(expected.getCalendar().getTimeInMillis());
        System.arraycopy(buffer, 0, bytes, 16, 8);

        buffer = BinaryUtils.valueOf(expected.getInstant().toEpochMilli());
        System.arraycopy(buffer, 0, bytes, 24, 8);

        assertEquals(expected.toString(), FastProto.parse(bytes, TimeObject.class).toString());
    }

    @Test
    public void testToBytes() {
        val expected = new byte[32];
        val obj = new TimeObject();

        var buffer = BinaryUtils.valueOf(obj.getDate().getTime());
        System.arraycopy(buffer, 0, expected, 0, 8);

        buffer = BinaryUtils.valueOf(obj.getTimestamp().getTime());
        System.arraycopy(buffer, 0, expected, 8, 8);

        buffer = BinaryUtils.valueOf(obj.getCalendar().getTimeInMillis());
        System.arraycopy(buffer, 0, expected, 16, 8);

        buffer = BinaryUtils.valueOf(obj.getInstant().toEpochMilli());
        System.arraycopy(buffer, 0, expected, 24, 8);

        assertArrayEquals(expected, FastProto.toBytes(obj, 32));
    }
}
