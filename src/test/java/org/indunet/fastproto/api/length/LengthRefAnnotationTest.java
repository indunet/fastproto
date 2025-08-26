package org.indunet.fastproto.api.length;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.BinaryType;
import org.indunet.fastproto.annotation.StringType;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LengthRefAnnotationTest {
    public static class PacketAnn1 {
        @UInt16Type(offset = 0)
        public int len;

        // length explicitly set to 4 but lengthRef points to len=5; decode should trust ref (5)
        @BinaryType(offset = 2, length = 4, lengthRef = "$len")
        public byte[] payload;
    }

    @Test
    public void testAnnotationLengthRefPrecedenceOnDecode() {
        byte[] bytes = new byte[] {
                0x05, 0x00,    // len=5
                1,2,3,4,5,6,7  // payload
        };
        val obj = FastProto.decode(bytes, PacketAnn1.class);
        assertEquals(5, obj.len);
        assertArrayEquals(new byte[]{1,2,3,4,5}, obj.payload);
    }

    public static class PacketAnn2 {
        @UInt16Type(offset = 10)
        public int nlen;

        // name at 0, use annotation-level lengthRef; length defaults to 0
        @StringType(offset = 0, lengthRef = "$nlen", charset = "UTF-8")
        public String name;
    }

    @Test
    public void testZeroLengthWithoutRefDecodeError() {
        class BadPacket {
            @StringType(offset = 0) // length=0 default, and no ref
            public String s;
        }
        assertThrows(org.indunet.fastproto.exception.CodecException.class, () -> FastProto.decode(new byte[0], BadPacket.class));
    }

    @Test
    public void testAnnotationLengthRefDecodeString() {
        byte[] bytes = new byte[12];
        // nlen at 10 = 3
        bytes[10] = 0x03; bytes[11] = 0x00;
        bytes[0] = 'A'; bytes[1] = 'B'; bytes[2] = 'C';
        val obj = FastProto.decode(bytes, PacketAnn2.class);
        assertEquals("ABC", obj.name);
        assertEquals(3, obj.nlen);
    }
} 