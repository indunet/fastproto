package org.indunet.fastproto.decoder;

import org.junit.Test;
import org.vnet.fastproto.exception.DecodeException;

public class ByteDecoderTest {
    NumberDecoder<?> decoder = new StandardInteger8Decoder();
    private byte[] datagram = {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};


    @Test
    public void get() throws DecodeException {
        assertEquals(decoder.get(datagram, 0), -2);
        assertEquals(decoder.get(datagram, 1), -1);
        assertEquals(decoder.get(datagram, 2), 0);
        assertEquals(decoder.get(datagram, 4), 2);
        assertEquals(decoder.get(datagram, 10), 8);
        assertEquals(decoder.get(datagram, 11), 9);
    }
}
