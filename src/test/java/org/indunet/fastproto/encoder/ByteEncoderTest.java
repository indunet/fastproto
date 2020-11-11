package org.indunet.fastproto.encoder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ByteEncoderTest {
    ByteEncoder encoder = new ByteEncoder();
    byte[] datagram = new byte[10];

    @Test
    public void testSet() {
        encoder.encode(datagram, 0, (byte) 1);
        encoder.encode(datagram, 1, (byte) -128);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);
    }
}
