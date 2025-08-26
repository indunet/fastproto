package org.indunet.fastproto.api.length;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.BinaryType;
import org.indunet.fastproto.annotation.LengthRef;
import org.indunet.fastproto.annotation.StringType;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LengthRefTest {
    public static class Packet1 {
        @UInt16Type(offset = 0)
        public int payloadLen;

        @BinaryType(offset = 2, length = 0)
        @LengthRef("payloadLen")
        public byte[] payload;
    }

    @Test
    public void testBinaryDecodeEncode() {
        byte[] bytes = new byte[] {
                0x05, 0x00,  // payloadLen = 5 (little-endian default)
                10, 11, 12, 13, 14
        };
        val pojo = FastProto.decode(bytes, Packet1.class);
        assertEquals(5, pojo.payloadLen);
        assertArrayEquals(new byte[]{10, 11, 12, 13, 14}, pojo.payload);

        // change payload and roundtrip encode
        pojo.payload = new byte[]{1,2,3};
        pojo.payloadLen = 3; // encoded length should follow field since useSelfOnEncode=false by default
        val encoded = FastProto.encode(pojo);
        assertEquals(2 + 3, encoded.length);
        assertArrayEquals(new byte[]{3,0,1,2,3}, encoded);
    }

    public static class Packet2 {
        @UInt16Type(offset = 100)
        public int strLen;

        @StringType(offset = 0, length = 0, charset = "UTF-8")
        @LengthRef(value = "strLen", useSelfOnEncode = true)
        public String name;
    }

    @Test
    public void testStringUseSelfOnEncode() {
        // decode: strLen provides length
        byte[] bytes = new byte[102];
        // strLen at offset 100 = 4
        bytes[100] = 0x04; bytes[101] = 0x00;
        // name at offset 0 length=4: "ABCD"
        bytes[0] = 'A'; bytes[1] = 'B'; bytes[2] = 'C'; bytes[3] = 'D';
        val obj = FastProto.decode(bytes, Packet2.class);
        assertEquals("ABCD", obj.name);
        assertEquals(4, obj.strLen);

        // encode: useSelfOnEncode=true should ignore strLen and use actual bytes length
        obj.name = "你好"; // UTF-8: 6 bytes
        obj.strLen = 4;    // deliberately inconsistent
        val encoded = FastProto.encode(obj);
        assertEquals(102, encoded.length);
        // Check first 6 bytes are UTF-8 of "你好"
        byte[] expectedHead = "你好".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        assertArrayEquals(expectedHead, java.util.Arrays.copyOfRange(encoded, 0, expectedHead.length));
    }

    public static class PacketBadOrder {
        @BinaryType(offset = 2, length = 0)
        @LengthRef("len")
        public byte[] payload;

        @UInt16Type(offset = 0)
        public int len; // declared after payload: should fail
    }

    @Test
    public void testOrderValidationFails() {
        assertThrows(ResolvingException.class, () -> FastProto.encode(new PacketBadOrder()));
        assertThrows(ResolvingException.class, () -> FastProto.decode(new byte[4], PacketBadOrder.class));
    }

    public static class PacketBounds {
        @UInt16Type(offset = 0)
        public int len;

        @BinaryType(offset = 2, length = 0)
        @LengthRef(value = "len", min = 1, max = 4)
        public byte[] payload;
    }

    @Test
    public void testMinMaxBounds() {
        // len=0 violates min=1 during decode
        byte[] bytes = new byte[] {0,0};
        assertThrows(org.indunet.fastproto.exception.CodecException.class, () -> FastProto.decode(bytes, PacketBounds.class));

        // encode with too long payload violates max
        PacketBounds p = new PacketBounds();
        p.len = 5; // out of bounds
        p.payload = new byte[]{1,2,3,4,5};
        assertThrows(org.indunet.fastproto.exception.EncodingException.class, () -> FastProto.encode(p));
    }
} 