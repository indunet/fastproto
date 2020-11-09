package org.indunet.fastproto.decoder;

import org.junit.Test;
import org.vnet.fastproto.exception.DecodeException;

public class BooleanDecoderTest {
    BooleanDecoder<?> decoder = new StandardBooleanDecoder();
    private byte[] datagram = {0x01, 0x02, 0x04, 0x08};

    @Test
    public void testGet() throws DecodeException {
        assertEquals(decoder.get(datagram, 0, 0), true);
        assertEquals(decoder.get(datagram, 0, 1), false);

        assertEquals(decoder.get(datagram, 1, 1), true);
        assertEquals(decoder.get(datagram, 2, 2), true);
        assertEquals(decoder.get(datagram, 3, 3), true);
    }
}
