package org.indunet.fastproto.encoder;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class BinaryEncoderTest {
    BinaryEncoder encoder = new BinaryEncoder();
    byte[] datagram = new byte[4];

    @Test
    public void testSet() {
        encoder.encode(datagram, 0, new byte[] {0x01, 0x02, 0x03, 0x04});

        assertArrayEquals(datagram, new byte[] {0x01, 0x02, 0x03, 0x04});
    }
}
