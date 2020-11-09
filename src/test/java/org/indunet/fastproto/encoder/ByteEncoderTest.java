package org.indunet.fastproto.encoder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ByteEncoderTest {
    NumberEncoder<Integer> encoder = new ByteEncoder();
    byte[] datagram = new byte[10];

    @Test
    public void testSet() {
        encoder.set(datagram, 0, 1);
        encoder.set(datagram, 1, -128);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);
    }
}
