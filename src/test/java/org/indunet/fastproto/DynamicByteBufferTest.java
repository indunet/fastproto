package org.indunet.fastproto;

import org.indunet.fastproto.exception.CodecException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test of DynamicByteBuffer
 *
 * @author Deng Ran
 * @since 3.8.4
 */
public class DynamicByteBufferTest {
    @Test
    public void testAppend() {
        DynamicByteBuffer byteBuffer = new DynamicByteBuffer();

        byteBuffer.append((byte) 1);
        byteBuffer.append((byte) 2);
        byteBuffer.append((byte) 3);
        byteBuffer.append((byte) 4);

        assertEquals(1, byteBuffer.get(0));
        assertEquals(2, byteBuffer.get(1));
        assertEquals(3, byteBuffer.get(2));
        assertEquals(4, byteBuffer.get(3));
    }

    @Test
    public void testSetAndGet() {
        DynamicByteBuffer byteBuffer = new DynamicByteBuffer();
        byte[] expected = {8, 9, 10, 11};

        byteBuffer.set(0, (byte) 8);
        byteBuffer.set(1, (byte) 9);
        byteBuffer.set(2, (byte) 10);
        byteBuffer.set(3, (byte) 11);

        assertEquals((byte) 11, byteBuffer.get(3));
        assertArrayEquals(expected, byteBuffer.getBytes());
        assertThrows(IndexOutOfBoundsException.class, () -> byteBuffer.get(16));
        assertThrows(CodecException.class, () -> byteBuffer.get(-1));
    }
}

