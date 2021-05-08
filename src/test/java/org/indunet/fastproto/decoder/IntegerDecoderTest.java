package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntegerDecoderTest {
    IntegerDecoder decoder = new IntegerDecoder();
    private byte[] datagram = {0, 1, 0, 1, -1, -1, -1, -1};

    @Test
    public void testDecode() {
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.Little), 256 + 256 * 256 * 256);
        assertEquals(decoder.decode(datagram, 4, EndianPolicy.Little), -1);

        assertEquals(decoder.decode(datagram, 0, EndianPolicy.Big), 0x00010001);
        assertEquals(decoder.decode(datagram, 4, EndianPolicy.Big), -1);
    }
}
