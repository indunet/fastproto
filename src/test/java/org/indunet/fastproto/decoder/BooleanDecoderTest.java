package org.indunet.fastproto.decoder;

import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BooleanDecoderTest {
    BooleanDecoder decoder = new BooleanDecoder();

    @Test
    public void testDecode1() {
        byte[] datagram = {0x01, 0x02, 0x04, 0x08};

        assertTrue(decoder.decode(datagram, 0, 0));
        assertFalse(decoder.decode(datagram, 0, 1));

        assertTrue(decoder.decode(datagram, 1, 1));
        assertTrue(decoder.decode(datagram, 2, 2));
        assertTrue(decoder.decode(datagram, 3, 3));
        assertTrue(decoder.decode(datagram, -1, 3));
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 2, 10));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -101, 0));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 0, -1));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, 0));
    }
}
