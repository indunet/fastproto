package org.indunet.fastproto.encoder;

import org.indunet.fastproto.Endian;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class LongEncoderTest {
    LongEncoder encoder = new LongEncoder();
    byte[] datagram = new byte[16];

    @Test
    public void testSet() {
        encoder.encode(datagram, 0, 0x0102030405060708L, Endian.Little);
        encoder.encode(datagram, 8, -1L, Endian.Little);

        assertArrayEquals(datagram, new byte[]{0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01,
                -1, -1, -1, -1, -1, -1, -1, -1});
    }
}
