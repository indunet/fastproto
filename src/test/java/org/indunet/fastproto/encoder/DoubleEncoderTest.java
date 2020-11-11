package org.indunet.fastproto.encoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class DoubleEncoderTest {
    DoubleEncoder encoder = new DoubleEncoder();
    byte[] datagram = new byte[8];
    double pi = 3.141, e = 2.718;

    @Test
    public void testSet() {
        encoder.encode(datagram, 0, e, Endian.Little);
        assertArrayEquals(datagram, NumberUtils.doubleToBinary(e));

        encoder.encode(datagram, 0, pi, Endian.Little);
        assertArrayEquals(datagram, NumberUtils.doubleToBinary(pi));
    }
}
