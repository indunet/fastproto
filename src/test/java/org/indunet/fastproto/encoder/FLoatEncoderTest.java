package org.indunet.fastproto.encoder;

import org.junit.Test;
import org.vnet.fastproto.utils.NumberUtils;

public class FLoatEncoderTest {
    NumberEncoder<Float> encoder = new FloatEncoder();
    byte[] datagram = new byte[4];
    float pi = 3.141f, e = 2.718f;

    @Test
    public void testSet() {
        encoder.set(datagram, 0, pi);
        assertArrayEquals(datagram, NumberUtils.floatToByteArray(pi));

        // TODO
        encoder.set(datagram, 0, e);
        assertArrayEquals(datagram, NumberUtils.floatToByteArray(e));
    }
}
