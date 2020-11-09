package org.indunet.fastproto.decoder;

import org.junit.Test;
import org.vnet.fastproto.Endian;
import org.vnet.fastproto.exception.DecodeException;

public class IntegerDecoderTest {
    NumberDecoder<?> decoder = new StandardIntegerDecoder();
    private byte[] datagram = {0, 1, 0, 1, -1, -1, -1, -1};

    @Test
    public void testGet() throws DecodeException {
        // For little endian.
        assertEquals(decoder.get(datagram, 0), 256 + 256 * 256 * 256);
        assertEquals(decoder.get(datagram, 4, Endian.Little), -1);

        // For Big endian.
        assertEquals(decoder.get(datagram, 0, Endian.Big), 0x00010001);
        assertEquals(decoder.get(datagram, 4, Endian.Big), -1);
    }
}
