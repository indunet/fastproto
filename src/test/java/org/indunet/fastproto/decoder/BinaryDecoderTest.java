package org.indunet.fastproto.decoder;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class BinaryDecoderTest {
    BinaryDecoder decoder = new BinaryDecoder();
    private byte[] datagram = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    @Test
    public void testDecode() {
        assertArrayEquals(decoder.decode(datagram, 0, 2), new byte[] {0, 1});
        assertArrayEquals(decoder.decode(datagram, 3, 4), new byte[] {3, 4, 5, 6});
        assertArrayEquals(decoder.decode(datagram, 5, 5), new byte[] {5, 6, 7, 8, 9});
    }
}
