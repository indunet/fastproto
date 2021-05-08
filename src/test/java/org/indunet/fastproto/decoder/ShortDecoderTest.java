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
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.Little), 256);
        assertEquals(decoder.decode(datagram, 2, EndianPolicy.Little), 2 + 3 * 256);
        assertEquals(decoder.decode(datagram, 4, EndianPolicy.Little), -1);
        assertEquals(decoder.decode(datagram, 6, EndianPolicy.Little), -2);

        // For big endian.
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.Big), 0x0001);
        assertEquals(decoder.decode(datagram, 2, EndianPolicy.Big), 0x0203);
    }
}
