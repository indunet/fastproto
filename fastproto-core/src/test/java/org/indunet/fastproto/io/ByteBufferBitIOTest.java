package org.indunet.fastproto.io;

import org.indunet.fastproto.BitOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for sequential bit IO cursor advancement on read/write.
 */
public class ByteBufferBitIOTest {
    @Test
    public void testSequentialReadBool_LSB0() {
        ByteBuffer buffer = new ByteBuffer(new byte[] {(byte) 0b1010_1101});
        ByteBufferInputStream in = new ByteBufferInputStream(buffer);

        // LSB_0 reads bits from bit0..bit7 in order
        assertTrue(in.readBool(BitOrder.LSB_0));   // bit0 = 1
        assertFalse(in.readBool(BitOrder.LSB_0));  // bit1 = 0
        assertTrue(in.readBool(BitOrder.LSB_0));   // bit2 = 1
        assertTrue(in.readBool(BitOrder.LSB_0));   // bit3 = 1
        assertFalse(in.readBool(BitOrder.LSB_0));  // bit4 = 0
        assertTrue(in.readBool(BitOrder.LSB_0));   // bit5 = 1
        assertFalse(in.readBool(BitOrder.LSB_0));  // bit6 = 0
        assertTrue(in.readBool(BitOrder.LSB_0));   // bit7 = 1

        // After consuming 8 bits, cursor should advance to next byte at bit0
        assertEquals(1, in.byteIndex);
        assertEquals(0, in.bitIndex);
    }

    @Test
    public void testSequentialReadBool_MSB0() {
        ByteBuffer buffer = new ByteBuffer(new byte[] {(byte) 0b1010_1101});
        ByteBufferInputStream in = new ByteBufferInputStream(buffer);

        // MSB_0 maps bitOffset logically but cursor still advances 0..7
        assertTrue(in.readBool(BitOrder.MSB_0));   // maps to bit7 (1)
        assertFalse(in.readBool(BitOrder.MSB_0));  // maps to bit6 (0)
        assertTrue(in.readBool(BitOrder.MSB_0));   // maps to bit5 (1)
        assertFalse(in.readBool(BitOrder.MSB_0));  // maps to bit4 (0)
        assertTrue(in.readBool(BitOrder.MSB_0));   // maps to bit3 (1)
        assertTrue(in.readBool(BitOrder.MSB_0));   // maps to bit2 (1)
        assertFalse(in.readBool(BitOrder.MSB_0));  // maps to bit1 (0)
        assertTrue(in.readBool(BitOrder.MSB_0));   // maps to bit0 (1)

        assertEquals(1, in.byteIndex);
        assertEquals(0, in.bitIndex);
    }

    @Test
    public void testSequentialWriteBool_LSB0() {
        ByteBufferOutputStream out = new ByteBufferOutputStream(new ByteBuffer(new byte[1]));

        // Write bits LSB-first: expect 1101_1011 (binary) == 0xDB
        out.writeBool(BitOrder.LSB_0, true);   // bit0 = 1
        out.writeBool(BitOrder.LSB_0, true);   // bit1 = 1
        out.writeBool(BitOrder.LSB_0, false);  // bit2 = 0
        out.writeBool(BitOrder.LSB_0, true);   // bit3 = 1
        out.writeBool(BitOrder.LSB_0, true);   // bit4 = 1
        out.writeBool(BitOrder.LSB_0, false);  // bit5 = 0
        out.writeBool(BitOrder.LSB_0, true);   // bit6 = 1
        out.writeBool(BitOrder.LSB_0, true);   // bit7 = 1

        assertArrayEquals(new byte[] {(byte) 0b1101_1011}, out.toByteBuffer().toBytes());
        assertEquals(1, out.byteIndex);
        assertEquals(0, out.bitIndex);
    }

    @Test
    public void testSequentialWriteBool_MSB0() {
        ByteBufferOutputStream out = new ByteBufferOutputStream(new ByteBuffer(new byte[1]));

        // When using MSB_0, writing true/false in sequence should set bits from msb to lsb
        // Sequence corresponds to bits: 1 0 1 0 1 1 0 1 => 0b1010_1101 == 0xAD
        out.writeBool(BitOrder.MSB_0, true);   // bit7 = 1
        out.writeBool(BitOrder.MSB_0, false);  // bit6 = 0
        out.writeBool(BitOrder.MSB_0, true);   // bit5 = 1
        out.writeBool(BitOrder.MSB_0, false);  // bit4 = 0
        out.writeBool(BitOrder.MSB_0, true);   // bit3 = 1
        out.writeBool(BitOrder.MSB_0, true);   // bit2 = 1
        out.writeBool(BitOrder.MSB_0, false);  // bit1 = 0
        out.writeBool(BitOrder.MSB_0, true);   // bit0 = 1

        assertArrayEquals(new byte[] {(byte) 0b1010_1101}, out.toByteBuffer().toBytes());
        assertEquals(1, out.byteIndex);
        assertEquals(0, out.bitIndex);
    }
} 