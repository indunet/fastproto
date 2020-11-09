package org.indunet.fastproto.decoder;

import org.junit.Test;
import org.vnet.fastproto.Endian;
import org.vnet.fastproto.exception.DecodeException;

public class ShortDecoderTest {
    NumberDecoder<?> decoder = new StandardInteger16Decoder();
    private byte[] datagram = {0, 1, 2, 3, -1, -1, -2, -1};

    @Test
    public void testGet() throws DecodeException {
        // For little endian.
        assertEquals(decoder.get(datagram, 0), 256);
        assertEquals(decoder.get(datagram, 2), 2 + 3 * 256);
        assertEquals(decoder.get(datagram, 4), -1);
        assertEquals(decoder.get(datagram, 6), -2);

        // For big endian.
        assertEquals(decoder.get(datagram, 0, Endian.Big), 0x0001);
        assertEquals(decoder.get(datagram, 2, Endian.Big), 0x0203);
    }
}
