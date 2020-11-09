package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShortDecoderTest {
    ShortDecoder decoder = new ShortDecoder();
    private byte[] datagram = {0, 1, 2, 3, -1, -1, -2, -1};

    @Test
    public void testDecode() {
        // For little endian.
        assertEquals(decoder.decode(datagram, 0, Endian.Little), 256);
        assertEquals(decoder.decode(datagram, 2, Endian.Little), 2 + 3 * 256);
        assertEquals(decoder.decode(datagram, 4, Endian.Little), -1);
        assertEquals(decoder.decode(datagram, 6, Endian.Little), -2);

        // For big endian.
        assertEquals(decoder.decode(datagram, 0, Endian.Big), 0x0001);
        assertEquals(decoder.decode(datagram, 2, Endian.Big), 0x0203);
    }
}
