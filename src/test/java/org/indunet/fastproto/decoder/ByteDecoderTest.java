package org.indunet.fastproto.decoder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ByteDecoderTest {
    ByteDecoder decoder = new ByteDecoder();
    private byte[] datagram = {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    @Test
    public void testDecode() {
        assertEquals(decoder.decode(datagram, 0), -2);
        assertEquals(decoder.decode(datagram, 1), -1);
        assertEquals(decoder.decode(datagram, 2), 0);
        assertEquals(decoder.decode(datagram, 4), 2);
        assertEquals(decoder.decode(datagram, 10), 8);
        assertEquals(decoder.decode(datagram, 11), 9);
    }
}
