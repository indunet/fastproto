package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongDecoderTest {
    LongDecoder decoder = new LongDecoder();
    private byte[] datagram;

    @Test
    public void testDecode() {
        datagram = new byte[] {-1, -1, -1, -1, -1, -1, -1, -1};     // mean -1
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE), -1L);

        datagram = new byte[] {0, 0, 0, 0, 1, 0, 0, 1};
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE), (long)Math.pow(256, 4) + (long)Math.pow(256, 7));
    }
}
