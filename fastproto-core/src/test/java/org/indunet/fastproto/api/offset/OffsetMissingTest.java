package org.indunet.fastproto.api.offset;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.BinaryType;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class OffsetMissingTest {
    public static class PacketPrimitive {
        // Neither offset nor offsetRef specified
        @UInt16Type()
        public int value;
    }

    @Test
    public void testPrimitiveMissingOffsetAndRefDecode() {
        assertThrows(ResolvingException.class, () -> FastProto.decode(new byte[2], PacketPrimitive.class));
    }

    @Test
    public void testPrimitiveMissingOffsetAndRefEncode() {
        val p = new PacketPrimitive();
        p.value = 1;
        assertThrows(ResolvingException.class, () -> FastProto.encode(p));
    }

    public static class PacketBinary {
        // Neither offset nor offsetRef specified
        @BinaryType(length = 2)
        public byte[] data;
    }

    @Test
    public void testBinaryMissingOffsetAndRefDecode() {
        assertThrows(ResolvingException.class, () -> FastProto.decode(new byte[2], PacketBinary.class));
    }

    @Test
    public void testBinaryMissingOffsetAndRefEncode() {
        val p = new PacketBinary();
        p.data = new byte[]{1, 2};
        assertThrows(ResolvingException.class, () -> FastProto.encode(p));
    }
}


