package org.indunet.fastproto.io;

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.BitOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void testReadBool_AdvanceAcrossBytes() {
        ByteBuffer buffer = new ByteBuffer(new byte[] {(byte) 0xFF, (byte) 0x01});
        ByteBufferInputStream in = new ByteBufferInputStream(buffer);

        for (int i = 0; i < 8; i++) {
            assertTrue(in.readBool(BitOrder.LSB_0));
        }
        // Next bit should be from next byte bit0
        assertTrue(in.readBool(BitOrder.LSB_0));
        assertEquals(1, in.bitIndex);
        assertEquals(1, in.byteIndex);
    }
}
