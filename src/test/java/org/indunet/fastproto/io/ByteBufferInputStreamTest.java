package org.indunet.fastproto.io;

import org.indunet.fastproto.ByteOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link ByteBufferInputStream}.
 */
public class ByteBufferInputStreamTest {
    @Test
    public void testReadDoubleBigEndian() {
        double expected = 3.1415926;
        ByteBufferOutputStream output = new ByteBufferOutputStream();
        output.writeDouble(2, ByteOrder.BIG, expected);

        ByteBufferInputStream input = new ByteBufferInputStream(output.toByteBuffer());
        double actual = input.readDouble(2, ByteOrder.BIG);

        assertEquals(expected, actual, 0.000001);
    }
}
