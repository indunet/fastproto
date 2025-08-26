package org.indunet.fastproto.api.struct;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StructArrayTest {
    @Data
    @DefaultByteOrder(ByteOrder.LITTLE)
    public static class Item {
        @UInt16Type(offset = 0)
        public int id;

        @Int32Type(offset = 2)
        public int value;

        public Item() {}
        public Item(int id, int value) { this.id = id; this.value = value; }
    }

    @Data
    @DefaultByteOrder(ByteOrder.LITTLE)
    public static class PacketArr {
        @UInt16Type(offset = 0)
        public int count;

        @StructArrayType(offset = 2, element = Item.class, lengthRef = "$count")
        public Item[] items;
    }

    @Test
    public void testStructArrayDecodeEncode_Array() {
        // element size = 2 (UInt16) + 4 (Int32) = 6
        byte[] bytes = new byte[2 + 2 * 6];
        // count = 2
        bytes[0] = 0x02; bytes[1] = 0x00;
        // item0: id=0x1234, value=0x78563412 (LE)
        bytes[2] = 0x34; bytes[3] = 0x12;
        bytes[4] = 0x12; bytes[5] = 0x34; bytes[6] = 0x56; bytes[7] = 0x78;
        // item1: id=0xABCD, value=0x0A0B0C0D (LE)
        int base = 2 + 6;
        bytes[base + 0] = (byte) 0xCD; bytes[base + 1] = (byte) 0xAB;
        bytes[base + 2] = 0x0D; bytes[base + 3] = 0x0C; bytes[base + 4] = 0x0B; bytes[base + 5] = 0x0A;

        val pkt = FastProto.decode(bytes, PacketArr.class);
        assertEquals(2, pkt.count);
        assertEquals(0x1234, pkt.items[0].id);
        assertEquals(0x78563412, pkt.items[0].value);
        assertEquals(0xABCD, pkt.items[1].id & 0xFFFF);
        assertEquals(0x0A0B0C0D, pkt.items[1].value);

        // encode roundtrip
        byte[] encoded = FastProto.encode(pkt);
        assertArrayEquals(bytes, encoded);
    }

    @Data
    @DefaultByteOrder(ByteOrder.LITTLE)
    public static class PacketList {
        @UInt16Type(offset = 0)
        public int count;

        @StructArrayType(offset = 2, length = 0, element = Item.class, lengthRef = "$count")
        public List<Item> items;
    }

    @Test
    public void testStructArrayDecodeEncode_List() {
        PacketList pkt = new PacketList();
        pkt.items = new ArrayList<>(Arrays.asList(new Item(1, 2), new Item(3, 4)));
        pkt.count = pkt.items.size();

        byte[] encoded = FastProto.encode(pkt);
        assertEquals(2 + 2 * 6, encoded.length);

        PacketList decoded = FastProto.decode(encoded, PacketList.class);
        assertEquals(2, decoded.count);
        assertEquals(1, decoded.items.get(0).id);
        assertEquals(2, decoded.items.get(0).value);
        assertEquals(3, decoded.items.get(1).id);
        assertEquals(4, decoded.items.get(1).value);
    }

    @Data
    @DefaultByteOrder(ByteOrder.LITTLE)
    public static class PacketFixed {
        @StructArrayType(offset = 0, length = 2, element = Item.class)
        public Item[] items;
    }

    @Test
    public void testStructArrayDecodeEncode_FixedLength() {
        // Two items, each 6 bytes, starting at offset 0
        byte[] bytes = new byte[2 * 6];
        // item0: id=0x0001, value=0x02030405 (LE)
        bytes[0] = 0x01; bytes[1] = 0x00;
        bytes[2] = 0x05; bytes[3] = 0x04; bytes[4] = 0x03; bytes[5] = 0x02;
        // item1: id=0x0010, value=0x0A0B0C0D (LE)
        bytes[6] = 0x10; bytes[7] = 0x00;
        bytes[8] = 0x0D; bytes[9] = 0x0C; bytes[10] = 0x0B; bytes[11] = 0x0A;

        val pkt = FastProto.decode(bytes, PacketFixed.class);
        assertEquals(2, pkt.items.length);
        assertEquals(0x0001, pkt.items[0].id);
        assertEquals(0x02030405, pkt.items[0].value);
        assertEquals(0x0010, pkt.items[1].id);
        assertEquals(0x0A0B0C0D, pkt.items[1].value);

        // encode roundtrip
        byte[] encoded = FastProto.encode(pkt);
        assertArrayEquals(bytes, encoded);
    }
} 