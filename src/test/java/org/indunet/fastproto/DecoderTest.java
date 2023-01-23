package org.indunet.fastproto;

import lombok.*;
import org.indunet.fastproto.annotation.DoubleType;
import org.indunet.fastproto.annotation.FloatType;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

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
        val actual = FastProto.parse(bytes)
                .defaultByteOrder(ByteOrder.BIG)
                .readByte("byte1")
                .readShort("short16")
                .readInt8("int8")
                .readInt16("int16")
                .readInt32("int32")
                .readInt64("int64")
                .getAsMap();

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
        val actual = FastProto.parse(bytes)
                .defaultByteOrder(ByteOrder.LITTLE)
                .readUInt8("uint8")
                .readUInt16("uint16")
                .readUInt32("uint32")
                .readUInt64("uint64")
                .getAsMap();

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

        val actual = FastProto.parse(bytes)
                .readFloat("float32")
                .readDouble("double64")
                .getAsMap();

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
        val actual = FastProto.parse(bytes)
                .defaultByteOrder(ByteOrder.BIG)
                .readByte("byte1", x -> x * 0.1)
                .readShort("short16", x -> x * 10)
                .readInt8("int8", x -> x * 0.2)
                .readInt16("int16", x -> x / 10)
                .readInt32("int32", x -> x / 100.0)
                .readInt64("int64", x -> x * 0.01)
                .getAsMap();

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
        val actual = FastProto.parse(bytes)
                .defaultByteOrder(ByteOrder.LITTLE)
                .readUInt8("uint8", x -> x * 0.1)
                .readUInt16("uint16", x -> x * 10)
                .readUInt32("uint32", x -> x / 100.0)
                .readUInt64("uint64", x ->  x.multiply(BigInteger.valueOf(10l)))
                .getAsMap();

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

        val actual = FastProto.parse(bytes)
                .readFloat("float32", x -> x * 0.1)
                .readDouble("double64", x -> x * 10)
                .getAsMap();

        assertEquals(3.14f * 0.1, actual.get("float32"));
        assertEquals(2.71 * 10, actual.get("double64"));
    }

    @Test
    public void testGetAsType() {
        val bytes = new byte[]{1, 2, 3, 0, 0, 0, 1, 0};

        assertEquals(true, FastProto.parse(bytes)
                .boolType(0, 0)
                .getAsBoolean());
        assertEquals(2, FastProto.parse(bytes)
                .readInt8(1)
                .getAsInt());
        assertEquals(3, FastProto.parse(bytes)
                .readInt16(2)
                .getAsInt());
        assertEquals(256, FastProto.parse(bytes)
                .readInt32(4, ByteOrder.BIG)
                .getAsInt());
    }

    @Test
    public void testAlign() {
        val bytes = new byte[] {0x01, 0x02, 0x11, 0x12};
        val first = FastProto.parse(bytes)
                .align(2)
                .readInt8()
                .getAsInt();
        val third = FastProto.parse(bytes)
                .readInt8()
                .align(2)
                .readInt8()
                .getAsInt();

        assertEquals(0x01, first);
        assertEquals(0x11, third);
    }

    @Test
    public void testSkip() {
        val bytes = new byte[] {0x01, 0x02, 0x03, 0x04};
        var actual = FastProto.parse(new byte[] {0x01, 0x02, 0x03, 0x04})
                .skip(2)
                .readInt8()
                .getAsInt();

        assertEquals(0x03, actual);

        actual = FastProto.parse(bytes)
                .skip()
                .readInt8()
                .getAsInt();

        assertEquals(0x02, actual);

        assertThrows(IllegalArgumentException.class, () -> FastProto.parse(bytes)
                .skip(-1)
                .readInt8()
                .getAsInt());
    }

    @Test
    public void testAsMap() {
        val bytes = new byte[] {1, 2, 3, 0, 0, 0, 0, 1};
        val map = FastProto.parse(bytes)
                .boolType(0, 0)
                .readInt8(1)
                .readInt16(2)
                .readUInt32(4, ByteOrder.BIG)
                .getAsMap();

        assertEquals(true, map.get("0"));
        assertEquals(2, map.get("1"));
        assertEquals(3, map.get("2"));
        assertEquals(1l, map.get("3"));
    }

    @Test
    public void testMapTo() {
        val bytes = new byte[] {78, 0, 8, 0};
        val expected = new Wheel(78, 8);
        val actual = FastProto.parse(bytes)
                .readInt8(0, "diameter")
                .readInt16(2, "thickness")
                .mapTo(Wheel.class);

        assertEquals(expected.toString(), actual.toString());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Wheel {
        Integer diameter;
        Integer thickness;
    }

    @Test
    public void testMapToArg() {
        val bytes = new byte[]{78, 0, 8, 0, 0, 0, 0, 81};
        val expected = new DataObject(78, 8, 81l);
        val actual = FastProto.parse(bytes)
                .readInt8(0, "f1")
                .readInt16(2, "f2")
                .readUInt32(4, ByteOrder.BIG, "f3")
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
