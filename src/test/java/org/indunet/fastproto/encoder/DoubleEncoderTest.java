package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class DoubleEncoderTest {
    DoubleEncoder encoder = new DoubleEncoder();
    byte[] datagram = new byte[8];
    double pi = 3.141, e = 2.718;

    @Test
    public void testSet() {
        encoder.encode(datagram, 0, e, EndianPolicy.Little);
        assertArrayEquals(datagram, NumberUtils.doubleToBinary(e));

        encoder.encode(datagram, 0, pi, EndianPolicy.Little);
        assertArrayEquals(datagram, NumberUtils.doubleToBinary(pi));
    }
}
