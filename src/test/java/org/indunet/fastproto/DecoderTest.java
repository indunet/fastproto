package org.indunet.fastproto;

import lombok.*;
import org.indunet.fastproto.annotation.DoubleType;
import org.indunet.fastproto.annotation.FloatType;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test of Decoder.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public class DecoderTest {
    @Test
    public void testRead1() {
        val bytes = new byte[] {
                0x01,
                0x01, 0x02,
                0x01,
                0x01, 0x02,
                0x03, 0x04, 0x05, 0x06,
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08
        };
        val actual = FastProto.decode(bytes)
                .defaultByteOrder(ByteOrder.BIG)
                .readByte("byte1")
                .readShort("short16")
                .readInt8("int8")
                .readInt16("int16")
                .readInt32("int32")
                .readInt64("int64")
                .getMap();

        assertEquals((byte) 0x01, actual.get("byte1"));
        assertEquals((short) 0x0102, actual.get("short16"));
        assertEquals(0x01, actual.get("int8"));
        assertEquals(0x0102, actual.get("int16"));
        assertEquals(0x03040506, actual.get("int32"));
        assertEquals(0x0102030405060708l, actual.get("int64"));
    }

    @Test
    public void testRead2() {
        val bytes = new byte[] {
                0x01,
                0x01, 0x02,
                0x03, 0x04, 0x05, 0x06,
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08
        };
        val actual = FastProto.decode(bytes)
                .defaultByteOrder(ByteOrder.LITTLE)
                .readUInt8("uint8")
                .readUInt16("uint16")
                .readUInt32("uint32")
                .readUInt64("uint64")
                .getMap();

        assertEquals(0x01, actual.get("uint8"));
        assertEquals(0x0201, actual.get("uint16"));
        assertEquals(0x06050403l, actual.get("uint32"));
        assertEquals(BigInteger.valueOf(0x0807060504030201l), actual.get("uint64"));
    }

    @Test
    public void testRead3() {
        val bytes = new byte[12];
        System.arraycopy(BinaryUtils.valueOf(3.14f), 0, bytes, 0, FloatType.SIZE);
        System.arraycopy(BinaryUtils.valueOf(2.71), 0, bytes, 4, DoubleType.SIZE);

        val actual = FastProto.decode(bytes)
                .readFloat("float32")
                .readDouble("double64")
                .getMap();

        assertEquals(3.14f, actual.get("float32"));
        assertEquals(2.71, actual.get("double64"));
    }

    @Test
    public void testRead4() {
        val bytes = new byte[] {
                0x01,
                0x01, 0x02,
                0x01,
                0x01, 0x02,
                0x03, 0x04, 0x05, 0x06,
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08
        };
        val actual = FastProto.decode(bytes)
                .defaultByteOrder(ByteOrder.BIG)
                .readByte("byte1", x -> x * 0.1)
                .readShort("short16", x -> x * 10)
                .readInt8("int8", x -> x * 0.2)
                .readInt16("int16", x -> x / 10)
                .readInt32("int32", x -> x / 100.0)
                .readInt64("int64", x -> x * 0.01)
                .getMap();

        assertEquals((byte) 0x01 * 0.1, actual.get("byte1"));
        assertEquals((short) 0x0102 * 10, actual.get("short16"));
        assertEquals(0x01 * 0.2, actual.get("int8"));
        assertEquals(0x0102 / 10, actual.get("int16"));
        assertEquals(0x03040506 / 100.0, actual.get("int32"));
        assertEquals(0x0102030405060708l * 0.01, actual.get("int64"));
    }

    @Test
    public void testRead5() {
        val bytes = new byte[] {
                0x01,
                0x01, 0x02,
                0x03, 0x04, 0x05, 0x06,
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08
        };
        val actual = FastProto.decode(bytes)
                .defaultByteOrder(ByteOrder.LITTLE)
                .readUInt8("uint8", x -> x * 0.1)
                .readUInt16("uint16", x -> x * 10)
                .readUInt32("uint32", x -> x / 100.0)
                .readUInt64("uint64", x ->  x.multiply(BigInteger.valueOf(10l)))
                .getMap();

        assertEquals(0x01 * 0.1, actual.get("uint8"));
        assertEquals(0x0201 * 10, actual.get("uint16"));
        assertEquals(0x06050403l / 100.0, actual.get("uint32"));
        assertEquals(BigInteger.valueOf(0x0807060504030201l).multiply(BigInteger.valueOf(10l)), actual.get("uint64"));
    }

    @Test
    public void testRead6() {
        val bytes = new byte[12];
        System.arraycopy(BinaryUtils.valueOf(3.14f), 0, bytes, 0, FloatType.SIZE);
        System.arraycopy(BinaryUtils.valueOf(2.71), 0, bytes, 4, DoubleType.SIZE);

        val actual = FastProto.decode(bytes)
                .readFloat("float32", x -> x * 0.1)
                .readDouble("double64", x -> x * 10)
                .getMap();

        assertEquals(3.14f * 0.1, actual.get("float32"));
        assertEquals(2.71 * 10, actual.get("double64"));
    }

    @Test
    public void testAlign() {
        val bytes = new byte[] {0x01, 0x02, 0x11, 0x12};
        val first = FastProto.decode(bytes)
                .align( 2)
                .readInt8("int8")
                .getMap();
        val third = FastProto.decode(bytes)
                .readInt8("int8-1")
                .align(2)
                .readInt8("int8-2")
                .getMap();

        assertEquals(0x01, first.get("int8"));
        assertEquals(0x11, third.get("int8-2"));
    }

    @Test
    public void testSkip() {
        val bytes = new byte[] {0x01, 0x02, 0x03, 0x04};
        var actual = FastProto.decode(new byte[] {0x01, 0x02, 0x03, 0x04})
                .skip(2)
                .readInt8("int8")
                .getMap();

        assertEquals(0x03, actual.get("int8"));

        actual = FastProto.decode(bytes)
                .skip()
                .readInt8("int8")
                .getMap();

        assertEquals(0x02, actual.get("int8"));
        assertThrows(IllegalArgumentException.class, () -> FastProto.decode(bytes)
                .skip(-1)
                .readInt8("int8")
                .getMap());
    }

    @Test
    public void testGetMap() {
        val bytes = new byte[] {1, 2, 3, 0, 0, 0, 0, 1};
        val map = FastProto.decode(bytes)
                .readBool("bool1", 0, 0)
                .readInt8("int8", 1)
                .readInt16("int16", 2)
                .readUInt32("uint32", 4, ByteOrder.BIG)
                .getMap();

        assertEquals(true, map.get("bool1"));
        assertEquals(2, map.get("int8"));
        assertEquals(3, map.get("int16"));
        assertEquals(1l, map.get("uint32"));
    }

    @Test
    public void testMapTo() {
        val expected = new TestObject();
        val actual = FastProto.decode(expected.toBytes())
                .readByte("byte8", 0)
                .readShort("short16", 1)
                .readInt8("int8", 3)
                .readInt16("int16", 4)
                .readInt32("int32", 6)
                .readInt64("int64", 10)
                .readFloat("float32", 18)
                .readDouble("double64", 22)
                .readUInt8("uint8", 30)
                .readUInt16("uint16", 31)
                .readUInt32("uint32", 33)
                .readUInt64("uint64", 37)
                .mapTo(TestObject.class);

        assertEquals(expected.toString(), actual.toString());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestObject {
        Byte byte8 = 1;
        Short short16 = 2;
        Integer int8 = 3;
        Integer int16 = 4;
        Integer int32 = 5;
        Long int64 = 6L;
        Float float32 = 3.14f;
        Double double64 = 2.71;
        Integer uint8 = 9;
        Integer uint16 = 10;
        Long uint32 = 11L;
        BigInteger uint64 = new BigInteger("12");

        @SneakyThrows
        public byte[] toBytes() {
            val stream = new ByteArrayOutputStream();

            stream.write(new byte[] {byte8});
            stream.write((byte) (short16 & 0xFF));
            stream.write((byte) ((short16 >> 8) & 0xFF));
            stream.write(new byte[] {int8.byteValue()});
            stream.write(BinaryUtils.int16Of(int16, ByteOrder.LITTLE));
            stream.write(BinaryUtils.valueOf(int32));
            stream.write(BinaryUtils.valueOf(int64));
            stream.write(BinaryUtils.valueOf(float32));
            stream.write(BinaryUtils.valueOf(double64));
            stream.write(new byte[] {uint8.byteValue()});
            stream.write(BinaryUtils.uint16Of(new int[] {uint16}, ByteOrder.LITTLE));
            stream.write(BinaryUtils.uint32Of(new long[] {uint32}, ByteOrder.LITTLE));
            stream.write(BinaryUtils.valueOf(uint64.longValue()));

            stream.flush();

            return stream.toByteArray();
        }
    }

    @Test
    public void testMapToArg() {
        val bytes = new byte[]{78, 0, 8, 0, 0, 0, 0, 81};
        val expected = new DataObject(78, 8, 81l);
        val actual = FastProto.decode(bytes)
                .readInt8("f1", 0)
                .readInt16("f2", 2)
                .readUInt32("f3", 4, ByteOrder.BIG)
                .mapTo(DataObject.class);

        assertEquals(expected.toString(), actual.toString());
    }

    @Data
    public static class DataObject {
        Integer f1;
        Integer f2;
        Long f3;

        public DataObject(Integer f1, Integer f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        public DataObject(Integer f1, Integer f2, Long f3) {
            this.f3 = f3;
            this.f2 = f2;
            this.f1 = f1;
        }
    }
}
