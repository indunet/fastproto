package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class FLoatEncoderTest {
    FloatEncoder encoder = new FloatEncoder();
    byte[] datagram = new byte[4];
    float pi = 3.141f, e = 2.718f;

    @Test
    public void testSet() {
        encoder.encode(datagram, 0, pi, EndianPolicy.Little);
        assertArrayEquals(datagram, NumberUtils.floatToBinary(pi));

        // TODO
        encoder.encode(datagram, 0, e, EndianPolicy.Little);
        assertArrayEquals(datagram, NumberUtils.floatToBinary(e));
    }
}
