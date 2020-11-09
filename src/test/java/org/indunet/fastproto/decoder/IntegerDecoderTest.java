package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntegerDecoderTest {
    IntegerDecoder decoder = new IntegerDecoder();
    private byte[] datagram = {0, 1, 0, 1, -1, -1, -1, -1};

    @Test
    public void testDecode() {
        assertEquals(decoder.decode(datagram, 0, Endian.Little), 256 + 256 * 256 * 256);
        assertEquals(decoder.decode(datagram, 4, Endian.Little), -1);

        assertEquals(decoder.decode(datagram, 0, Endian.Big), 0x00010001);
        assertEquals(decoder.decode(datagram, 4, Endian.Big), -1);
    }
}
