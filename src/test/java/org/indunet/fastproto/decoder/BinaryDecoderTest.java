package org.indunet.fastproto.decoder;

import org.junit.Test;
import org.vnet.fastproto.exception.DecodeException;

import static org.junit.Assert.assertArrayEquals;

public class BinaryDecoderTest {
    ByteArrayDecoder<?> decoder = new StandardByteArrayDecoder();
    private byte[] datagram = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    @Test
    public void testGet() throws DecodeException {
        assertArrayEquals((byte[])decoder.get(datagram, 0, 2), new byte[] {0, 1});
        assertArrayEquals((byte[])decoder.get(datagram, 3, 4), new byte[] {3, 4, 5, 6});
        assertArrayEquals((byte[])decoder.get(datagram, 5, 5), new byte[] {5, 6, 7, 8, 9});
    }
}
