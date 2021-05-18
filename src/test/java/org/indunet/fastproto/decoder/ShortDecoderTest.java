package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShortDecoderTest {
    ShortDecoder decoder = new ShortDecoder();
    private byte[] datagram = {0, 1, 2, 3, -1, -1, -2, -1};

    @Test
    public void testDecode() {
        // For little endian.
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE), 256);
        assertEquals(decoder.decode(datagram, 2, EndianPolicy.LITTLE), 2 + 3 * 256);
        assertEquals(decoder.decode(datagram, 4, EndianPolicy.LITTLE), -1);
        assertEquals(decoder.decode(datagram, 6, EndianPolicy.LITTLE), -2);

        // For big endian.
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.BIG), 0x0001);
        assertEquals(decoder.decode(datagram, 2, EndianPolicy.BIG), 0x0203);
    }
}
