package org.indunet.fastproto.expect;

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.Expect;
import org.indunet.fastproto.annotation.Expects;
import org.indunet.fastproto.exception.DecodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpectFlowDecodeTest {
    public static class Packet1 {
        @Expect(offset = 0, value = 0xA5, size = 1)
        transient int _magic;

        @Expect(offset = 1, bytes = {(byte) 0xAA, (byte) 0x55})
        transient int _header;
    }

    public static class Packet2 {
        // big-endian 0x12 0x34 at offset 0
        @Expect(offset = 0, value = 0x1234, size = 2, byteOrder = ByteOrder.BIG)
        transient int _ver;
    }

    public static class Packet3 {
        @Expects({
                @Expect(offset = 0, value = 0x7E, size = 1),
                @Expect(offset = 1, bytes = {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC})
        })
        transient int _expects;
    }

    @Test
    public void testDecodePass() {
        byte[] bytes = new byte[]{
                (byte) 0xA5, (byte) 0xAA, (byte) 0x55
        };
        assertDoesNotThrow(() -> FastProto.decode(bytes, Packet1.class));
    }

    @Test
    public void testDecodeFailByte() {
        byte[] bytes = new byte[]{
                (byte) 0x00, (byte) 0xAA, (byte) 0x55
        };
        assertThrows(DecodingException.class, () -> FastProto.decode(bytes, Packet1.class));
    }

    @Test
    public void testDecodeValueSizeByteOrder() {
        // 0x12 0x34 BE
        byte[] ok = new byte[]{(byte) 0x12, (byte) 0x34};
        byte[] bad = new byte[]{(byte) 0x34, (byte) 0x12};
        assertDoesNotThrow(() -> FastProto.decode(ok, Packet2.class));
        assertThrows(DecodingException.class, () -> FastProto.decode(bad, Packet2.class));
    }

    @Test
    public void testDecodeMultipleExpects() {
        byte[] ok = new byte[]{(byte) 0x7E, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC};
        byte[] bad = new byte[]{(byte) 0x7E, (byte) 0xAA, (byte) 0xBC, (byte) 0xCC};
        assertDoesNotThrow(() -> FastProto.decode(ok, Packet3.class));
        assertThrows(DecodingException.class, () -> FastProto.decode(bad, Packet3.class));
    }
}


