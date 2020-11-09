package org.indunet.fastproto.encoder;

import org.junit.Test;
import org.vnet.fastproto.exception.EncodeException;

import static org.junit.Assert.assertArrayEquals;

public class LongEncoderTest {
    NumberEncoder<Long> encoder = new LongEncoder();
    byte[] datagram = new byte[16];

    @Test
    public void testSet() throws EncodeException {
        encoder.set(datagram, 0, 0x0102030405060708L);
        encoder.set(datagram, 8, -1L);

        assertArrayEquals(datagram, new byte[] {0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01,
                        -1, -1, -1, -1, -1, -1, -1, -1});
    }
}
