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

import org.indunet.fastproto.exception.CodecException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test of ByteBuffer.
 *
 * @author Deng Ran
 * @since 3.8.4
 */
public class ByteBufferTest {
    @Test
    public void testWrite() {
        ByteBuffer byteBuffer = new ByteBuffer();

        byteBuffer.set(3, (byte) 0x01);
        byteBuffer.set(4, (byte) 0x10);

        assertArrayEquals(new byte[] {0, 0, 0, 0x01, 0x10}, byteBuffer.toBytes());
    }

    @Test
    public void testRead() {
        ByteBuffer byteBuffer = new ByteBuffer();

        byteBuffer.set(2, (byte) 0x10);
        byteBuffer.set(3, (byte) 0x20);

        assertEquals((byte) 0x10, byteBuffer.get(2));
        assertEquals((byte) 0x20, byteBuffer.get(3));
    }

    @Test
    public void testOrEq() {
        ByteBuffer byteBuffer = new ByteBuffer();

        byteBuffer.set(0, (byte) 0x01);
        byteBuffer.orEq(0, (byte) 0x02);

        assertArrayEquals(new byte[] {0x03}, byteBuffer.toBytes());
    }

    @Test
    public void testAndEq() {
        ByteBuffer byteBuffer = new ByteBuffer();

        byteBuffer.set(0, (byte) 0x11);
        byteBuffer.andEq(0, (byte) 0x10);

        assertArrayEquals(new byte[] {0x10}, byteBuffer.toBytes());
    }

    @Test
    public void testSetAndGet() {
        ByteBuffer byteBuffer = new ByteBuffer();
        byte[] expected = {8, 9, 10, 11};

        byteBuffer.set(0, (byte) 8);
        byteBuffer.set(1, (byte) 9);
        byteBuffer.set(2, (byte) 10);
        byteBuffer.set(3, (byte) 11);

        assertEquals((byte) 11, byteBuffer.get(3));
        assertArrayEquals(expected, byteBuffer.toBytes());
        assertThrows(IndexOutOfBoundsException.class, () -> byteBuffer.get(16));
        assertThrows(CodecException.class, () -> byteBuffer.get(-1));
    }


}

