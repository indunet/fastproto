package org.indunet.fastproto.api.offset;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.BinaryType;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OffsetRefTest {
    public static class Packet1 {
        @UInt16Type(offset = 0)
        public int payloadPos; // absolute byte position

        @BinaryType(offset = 0, offsetRef = "$payloadPos", length = 3)
        public byte[] payload;
    }

    @Test
    public void testDecodeWithOffsetRef() {
        byte[] bytes = new byte[] {
                2, 0, // payloadPos = 2 (little-endian default)
                10, 11, 12
        };
        val p = FastProto.decode(bytes, Packet1.class);
        assertEquals(2, p.payloadPos);
        assertArrayEquals(new byte[]{10, 11, 12}, p.payload);
    }

    @Test
    public void testEncodeWithOffsetRef() {
        val p = new Packet1();
        p.payloadPos = 4;
        p.payload = new byte[]{1,2,3};
        byte[] encoded = FastProto.encode(p);
        // Expect at least the header (2 bytes) plus padding up to pos + len
        assertTrue(encoded.length >= 4 + 3);
        assertEquals(4, (encoded[0] & 0xFF) | ((encoded[1] & 0xFF) << 8)); // payloadPos LE
        assertArrayEquals(new byte[]{1,2,3}, new byte[]{encoded[4], encoded[5], encoded[6]});
    }

    public static class PacketBadOrder {
        @BinaryType(offset = 0, offsetRef = "$pos", length = 2)
        public byte[] payload;

        @UInt16Type(offset = 0)
        public int pos; // declared after payload: should fail
    }

    @Test
    public void testOffsetRefOrderValidationFails() {
        assertThrows(ResolvingException.class, () -> FastProto.decode(new byte[4], PacketBadOrder.class));
        assertThrows(ResolvingException.class, () -> FastProto.encode(new PacketBadOrder()));
    }

    // -------- Primitive type with offsetRef --------
    public static class Packet2 {
        @UInt16Type(offset = 0)
        public int pos;

        @UInt16Type(offset = 0, offsetRef = "$pos")
        public int value;
    }

    @Test
    public void testDecodeWithOffsetRefOnPrimitive() {
        // pos = 2, value at bytes [2..3] = 0xABCD (LE -> CD AB)
        byte[] bytes = new byte[] {
                2, 0,
                (byte) 0xCD, (byte) 0xAB
        };
        val p = FastProto.decode(bytes, Packet2.class);
        assertEquals(2, p.pos);
        assertEquals(0xABCD, p.value);
    }

    @Test
    public void testEncodeWithOffsetRefOnPrimitive() {
        val p = new Packet2();
        p.pos = 4;
        p.value = 0x0A0B;

        byte[] encoded = FastProto.encode(p);
        // Expect at least header (2 bytes) plus padding up to pos + sizeof(UInt16)=2
        assertTrue(encoded.length >= 6);
        // pos written at 0..1 (LE)
        assertEquals(4, (encoded[0] & 0xFF) | ((encoded[1] & 0xFF) << 8));
        // value written at offset 4..5 (LE -> low, high)
        assertArrayEquals(new byte[]{0x0B, 0x0A}, new byte[]{encoded[4], encoded[5]});
    }
}


