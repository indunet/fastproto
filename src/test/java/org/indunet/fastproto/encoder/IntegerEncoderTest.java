package org.indunet.fastproto.encoder;

import org.junit.Test;
import org.vnet.fastproto.Endian;
import org.vnet.fastproto.exception.EncodeException;

import static org.junit.Assert.assertArrayEquals;

public class IntegerEncoderTest {
    NumberEncoder<Integer> encoder = new ShortEncoder();
    byte[] datagram = new byte[4];

    @Test
    public void testSet() throws EncodeException {
        // Default as little endian.
        encoder.set(datagram, 0, 0x0102);
        encoder.set(datagram, 2, 0x0304);

        assertArrayEquals(datagram, new byte[] {0x02, 0x01, 0x04, 0x03});

        encoder.set(datagram, 0, Endian.Big, 0x0102);
        encoder.set(datagram, 2, Endian.Big, 0x0304);

        assertArrayEquals(datagram, new byte[] {0x01, 0x02, 0x03, 0x04});

    }

    @Test(expected = EncodeException.class)
    public void testSetEncodeException() throws EncodeException {
        encoder.set(datagram, 0, 0xFFFF);
    }
}
