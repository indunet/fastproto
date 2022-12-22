package org.indunet.fastproto;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DynamicByteArrayTest {
    @Test
    public void testWriteAndRead() {
        DynamicByteArray array = new DynamicByteArray();
        array.write((byte) 1);
        array.write((byte) 2);
        array.write((byte) 3);
        array.write((byte) 4);
        assertEquals(1, array.read(0));
        assertEquals(2, array.read(1));
        assertEquals(3, array.read(2));
        assertEquals(4, array.read(3));
    }

    @Test
    public void testWriteAndReadAtOffset() {
        DynamicByteArray array = new DynamicByteArray();
        array.write((byte) 1, 0);
        array.write((byte) 2, 1);
        array.write((byte) 3, 2);
        array.write((byte) 4, 3);

        assertEquals(1, array.read(0));
        assertEquals(2, array.read(1));
        assertEquals(3, array.read(2));
        assertEquals(4, array.read(3));
    }

    @Test
    public void testGetBytes() {
        DynamicByteArray array = new DynamicByteArray();
        array.write((byte) 1);
        array.write((byte) 2);
        array.write((byte) 3);
        array.write((byte) 4);

        byte[] expected = {1, 2, 3, 4};
        assertArrayEquals(expected, array.getBytes());
    }
}
