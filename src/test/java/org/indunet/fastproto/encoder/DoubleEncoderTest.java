package org.indunet.fastproto.encoder;

import org.junit.Test;
import org.vnet.fastproto.utils.NumberUtils;

public class DoubleEncoderTest {
    NumberEncoder<Double> encoder = new DoubleEncoder();
    byte[] datagram = new byte[8];
    double pi = 3.141, e = 2.718;

    @Test
    public void testSet() {
        encoder.set(datagram, 0, e);
        assertArrayEquals(datagram, NumberUtils.doubleToByteArray(e));

        encoder.set(datagram, 0, pi);
        assertArrayEquals(datagram, NumberUtils.doubleToByteArray(pi));
    }
}
