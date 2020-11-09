package org.indunet.fastproto.decoder;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanDecoderTest {
    BooleanDecoder decoder = new BooleanDecoder();
    private byte[] datagram = {0x01, 0x02, 0x04, 0x08};

    @Test
    public void testDecode() {
        assertEquals(decoder.decode(datagram, 0, 0), true);
        assertEquals(decoder.decode(datagram, 0, 1), false);

        assertEquals(decoder.decode(datagram, 1, 1), true);
        assertEquals(decoder.decode(datagram, 2, 2), true);
        assertEquals(decoder.decode(datagram, 3, 3), true);
    }
}
