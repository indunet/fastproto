package org.indunet.fastproto;

import lombok.val;
import lombok.var;
import org.indunet.fastproto.annotation.DoubleType;
import org.indunet.fastproto.annotation.FloatType;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of Encoder
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public class EncoderTest {
    @Test
    public void testGet1() {
        val expected = new byte[]{1, 0, 3, 0, 4, 0, 0, 0, 0, 32};
        val actual = FastProto.toBytes(expected.length)
                .writeUInt8(0, 1)
                .writeUInt16(2, 3, 4)
                .writeUInt32(6, ByteOrder.BIG, 32)
                .get();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGet2() {
        val expected = new byte[16];

        expected[0] |= 0x01;
        expected[1] = 1;
        expected[3] = 3;
        System.arraycopy(BinaryUtils.valueOf(1.0f), 0, expected, 4, FloatType.SIZE);
        System.arraycopy(BinaryUtils.valueOf(1.1), 0, expected, 8, DoubleType.SIZE);

        var actual = FastProto.toBytes(expected.length)
                .boolType(0, 0, true)
                .writeInt8(1, 1)
                .writeInt16(2, ByteOrder.BIG, 3)
                .writeFloat(4, 1.0f)
                .writeDouble(8, 1.1)
                .get();

        assertArrayEquals(expected, actual);

        actual = FastProto.toBytes()
                .boolType(0, 0, true)
                .writeInt8(1, 1)
                .writeInt16(2, ByteOrder.BIG, 3)
                .writeFloat(4, 1.0f)
                .writeDouble(8, 1.1)
                .get();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testWriteAndAppend1() {
        val actual = FastProto.toBytes()
                .appendByte((byte) 0x01)
                .align(2)
                .appendShort((short) 0x0102)
                .appendInt8(0x01, 0x02)
                .appendInt16(0x0102, 0x0304)
                .appendInt32(0x01020304)
                .appendInt64(0x0102030405060708l)
                .get();
        val expected = new byte[]{0x01, 0, 0x02, 0x01, 0x01, 0x02, 0x02, 0x01, 0x04, 0x03, 0x04, 0x03, 0x02, 0x01,
                0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01};

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testWriteAndAppend2() {
        val actual = FastProto.toBytes()
                .appendUInt8(0x01, 0x02)
                .appendUInt16(0x0102, 0x0304)
                .appendUInt32(0x01020304)
                .appendUInt64(BigInteger.valueOf(0x0102030405060708l))
                .get();
        val expected = new byte[]{0x01, 0x02, 0x02, 0x01, 0x04, 0x03, 0x04, 0x03, 0x02, 0x01,
                0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01};

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testWriteAndAppend3() {
        val actual = FastProto.toBytes()
                .appendFloat(3.14f)
                .appendDouble(2.71)
                .get();
        val expected = new byte[12];

        System.arraycopy(BinaryUtils.valueOf(3.14f), 0, expected, 0, 4);
        System.arraycopy(BinaryUtils.valueOf(2.71), 0, expected, 4, 8);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAlign() {
        val bytes = FastProto.toBytes()
                .writeInt8(2, (byte) 0x10)
                .align(8)
                .get();

        assertEquals(8, bytes.length);
    }
}
