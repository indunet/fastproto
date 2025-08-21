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

package org.indunet.fastproto.io;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests of ByteBufferIOStream.
 *
 * @author Deng Ran
 * @since 3.10.2
 */
public class ByteBufferIOStreamTest {
    public static class MockByteBufferOutputStream extends ByteBufferIOStream {
        public MockByteBufferOutputStream() {
            super(new ByteBuffer());
        }

        public void append(byte value) {
            this.byteBuffer.set(byteIndex ++, value);
        }
    }

    @Test
    public void testAlign() {
        val stream = new MockByteBufferOutputStream();

        stream.append((byte) 0x01);
        stream.append((byte) 0x02);
        stream.append((byte) 0x03);
        stream.align(8);
        stream.append((byte) 0x01);
        stream.append((byte) 0x02);

        assertEquals(10, stream.toByteBuffer()
                .toBytes()
                .length);
    }

    @Test
    public void testAlignPowerOfTwoValidation() {
        val stream = new MockByteBufferOutputStream();

        // Valid power-of-two alignments should work
        stream.align(1);
        stream.align(2);
        stream.align(4);
        stream.align(8);
        stream.align(16);
        stream.align(32);

        // Invalid alignments should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> stream.align(0));
        assertThrows(IllegalArgumentException.class, () -> stream.align(-1));
        assertThrows(IllegalArgumentException.class, () -> stream.align(3));
        assertThrows(IllegalArgumentException.class, () -> stream.align(5));
        assertThrows(IllegalArgumentException.class, () -> stream.align(6));
        assertThrows(IllegalArgumentException.class, () -> stream.align(7));
        assertThrows(IllegalArgumentException.class, () -> stream.align(9));
        assertThrows(IllegalArgumentException.class, () -> stream.align(10));
        assertThrows(IllegalArgumentException.class, () -> stream.align(12));
        assertThrows(IllegalArgumentException.class, () -> stream.align(15));
    }

    @Test
    public void testAlignBehavior() {
        val stream = new MockByteBufferOutputStream();

        // Test alignment from various starting positions
        stream.append((byte) 0x01); // byteIndex = 1
        stream.align(4); // Should align to 4
        assertEquals(4, stream.byteIndex);

        stream.append((byte) 0x02); // byteIndex = 5
        stream.align(8); // Should align to 8
        assertEquals(8, stream.byteIndex);

        // Test alignment when already aligned
        stream.align(8); // Should stay at 8
        assertEquals(8, stream.byteIndex);
    }

    @Test
    public void testSkip() {
        val stream = new MockByteBufferOutputStream();

        stream.append((byte) 0x01);
        stream.skip(6);
        stream.skip();
        stream.append((byte) 0x02);
        stream.append((byte) 0x03);

        assertEquals(10, stream.toByteBuffer()
                .toBytes()
                .length);
        assertThrows(IllegalArgumentException.class, () -> FastProto.create()
                .skip(-1)
                .get());
    }
}
