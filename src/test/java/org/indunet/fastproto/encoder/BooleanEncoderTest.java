package org.indunet.fastproto.encoder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BooleanEncoderTest {
    BooleanEncoder encoder = new BooleanEncoder();
    byte[] datagram = new byte[10];

    @Test
    public void testSet() {
        encoder.set(datagram, 0, 0, true);
        encoder.set(datagram, 1, 7, true);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);

        encoder.set(datagram, 0, 0, false);
        encoder.set(datagram, 1, 7, false);

        assertEquals(datagram[0], 0);
        assertEquals(datagram[1], 0);
    }
}
