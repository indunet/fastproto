/*
 * Copyright 2019-2025 indunet.org
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

package org.indunet.fastproto.api.bcd;

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.BcdType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test for @BcdType using FastProto encode/decode.
 *
 * @author Deng Ran
 * @since 3.13.0
 */
public class BcdTypeTest {
    public static class Frame {
        @BcdType(offset = 0, length = 2, byteOrder = ByteOrder.BIG)
        int bigEndianValue;

        @BcdType(offset = 2, length = 2, byteOrder = ByteOrder.LITTLE)
        int littleEndianValue;
    }

    @Test
    public void testEncodeDecode() {
        Frame frame = new Frame();
        frame.bigEndianValue = 1234;
        frame.littleEndianValue = 9876;

        byte[] bytes = FastProto.encode(frame, 4);
        Frame copy = FastProto.decode(bytes, Frame.class);

        assertEquals(1234, copy.bigEndianValue);
        assertEquals(9876, copy.littleEndianValue);
    }
}


