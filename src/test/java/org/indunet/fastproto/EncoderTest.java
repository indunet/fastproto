package org.indunet.fastproto;

import lombok.val;
import lombok.var;
import org.indunet.fastproto.annotation.DoubleType;
import org.indunet.fastproto.annotation.FloatType;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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
        val actual = FastProto.toBytes()
                .length(expected.length)
                .uint8Type(0, 1)
                .uint16Type(2, 3, 4)
                .uint32Type(6, ByteOrder.BIG, 32)
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

        var actual = FastProto.toBytes()
                .length(expected.length)
                .boolType(0, 0, true)
                .int8Type(1, 1)
                .int16Type(2, ByteOrder.BIG, 3)
                .floatType(4, 1.0f)
                .doubleType(8, 1.1)
                .get();

        assertArrayEquals(expected, actual);

        actual = FastProto.toBytes()
                .boolType(0, 0, true)
                .int8Type(1, 1)
                .int16Type(2, ByteOrder.BIG, 3)
                .floatType(4, 1.0f)
                .doubleType(8, 1.1)
                .get();

        assertArrayEquals(expected, actual);
    }
}
