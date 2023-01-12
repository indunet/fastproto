package org.indunet.fastproto.codec;

import org.indunet.fastproto.BitOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Unit test of bool array type codec.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public class BoolArrayCodecTest {
    BoolArrayCodec codec = new BoolArrayCodec();

    @Test
    public void testDecode1() {
        byte[] datas = new byte[] {(byte) 0b10101010};
        boolean[] expected = new boolean[] {true, false, true, false, true, false, true, false};
        boolean[] actual = codec.decode(datas, 0, 0, 8, BitOrder.MSB_0);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDecode2() {
        byte[] datas = new byte[] { (byte) 0b0000_0000 };
        boolean[] actual = codec.decode(datas, 0, 0, 1, BitOrder.MSB_0);
        assertArrayEquals(new boolean[] { false }, actual);

        datas = new byte[] { (byte) 0b1000_1001, (byte) 0b0000_0010 };
        actual = codec.decode(datas, 0, 0, 5, BitOrder.MSB_0);
        assertArrayEquals(new boolean[] { true, false, false, false, true }, actual);

        datas = new byte[] { (byte) 0b0000_0000, (byte) 0b0000_0001 };
        actual = codec.decode(datas, 1, 7, 1, BitOrder.MSB_0);
        assertArrayEquals(new boolean[] { true }, actual);

        datas = new byte[] { (byte) 0b0000_0000, (byte) 0b0000_0001 };
        actual = codec.decode(datas, 1, 0, 1, BitOrder.LSB_0);
        assertArrayEquals(new boolean[] { true }, actual);
    }

    @Test
    public void testEncode1() {
        byte[] expected = new byte[] {(byte) 0b1010_1010};
        byte[] actual = new byte[1];
        boolean[] datas = new boolean[] {true, false, true, false, true, false, true, false};

        codec.encode(actual, 0, 0, BitOrder.MSB_0, datas);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEncode2() {
        byte[] actual = new byte[2];

        codec.encode(actual, 0, 3, BitOrder.MSB_0, new boolean[] { true, false, true, false, true });
        assertArrayEquals(new byte[] { (byte) 0b0001_0101, (byte) 0b0000_0000 }, actual);

        actual = new byte[2];
        codec.encode(actual, 1, 0, BitOrder.MSB_0, new boolean[] { true });
        assertArrayEquals(new byte[] { (byte) 0b0000_0000, (byte) 0b1000_0000 }, actual);

        actual = new byte[2];
        codec.encode(actual, 1, 7, BitOrder.LSB_0, new boolean[] { true });
        assertArrayEquals(new byte[] { (byte) 0b0000_0000, (byte) 0b1000_0000 }, actual);
    }
}
