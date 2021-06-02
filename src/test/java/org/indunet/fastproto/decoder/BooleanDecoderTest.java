package org.indunet.fastproto.decoder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooleanDecoderTest {
    BooleanDecoder decoder = new BooleanDecoder();

    @Test
    public void testDecode() {
        byte[] datagram = {0x01, 0x02, 0x04, 0x08};

        assertTrue(decoder.decode(datagram, 0, 0));
        assertFalse(decoder.decode(datagram, 0, 1));

        assertTrue(decoder.decode(datagram, 1, 1));
        assertTrue(decoder.decode(datagram, 2, 2));
        assertTrue(decoder.decode(datagram, 3, 3));
    }
}
