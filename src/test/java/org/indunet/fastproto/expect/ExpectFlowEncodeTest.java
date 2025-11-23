package org.indunet.fastproto.expect;

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.Expect;
import org.indunet.fastproto.exception.EncodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpectFlowEncodeTest {
    public static class Packet {
        @Expect(offset = 0, value = 0xA5, size = 1)
        transient int _magic;

        @Expect(offset = 1, bytes = {(byte) 0xAA, (byte) 0x55})
        transient int _header;

        @Expect(offset = 3, value = 0x00010002L, size = 4, byteOrder = ByteOrder.LITTLE)
        transient int _version;
    }

    public static class PacketBE {
        @Expect(offset = 0, value = 0x1234, size = 2, byteOrder = ByteOrder.BIG)
        transient int _be;
    }

    public static class PacketBad {
        @Expect(offset = 0) // neither bytes nor value
        transient int _bad;
    }

    @Test
    public void testEncodeWritesConstants() {
        byte[] out = FastProto.encode(new Packet(), 8);
        assertEquals(0xA5, out[0] & 0xFF);
        assertEquals(0xAA, out[1] & 0xFF);
        assertEquals(0x55, out[2] & 0xFF);
        // LITTLE endian 0x00010002 -> 02 00 01 00 at offset 3..6
        assertEquals(0x02, out[3] & 0xFF);
        assertEquals(0x00, out[4] & 0xFF);
        assertEquals(0x01, out[5] & 0xFF);
        assertEquals(0x00, out[6] & 0xFF);
    }

    @Test
    public void testEncodeBE2Bytes() {
        byte[] out = FastProto.encode(new PacketBE(), 2);
        // BIG endian 0x1234 -> 0x12 0x34
        assertEquals(0x12, out[0] & 0xFF);
        assertEquals(0x34, out[1] & 0xFF);
    }

    @Test
    public void testEncodeMissingValue() {
        assertThrows(EncodingException.class, () -> FastProto.encode(new PacketBad(), 1));
    }
}


