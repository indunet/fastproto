package org.indunet.fastproto.decoder;

import org.junit.Test;
import org.vnet.fastproto.exception.DecodeException;

public class LongDecoderTest {
    NumberDecoder<?> decoder = new StandardLongDecoder();
    private byte[] datagram;

    @Test
    public void testGet() throws DecodeException {
        datagram = new byte[] {-1, -1, -1, -1, -1, -1, -1, -1};     // mean -1
        assertEquals(decoder.get(datagram, 0), -1L);

        datagram = new byte[] {0, 0, 0, 0, 1, 0, 0, 1};
        assertEquals(decoder.get(datagram, 0), (long)Math.pow(256, 4) + (long)Math.pow(256, 7));
    }
}
