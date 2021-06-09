package org.indunet.fastproto.decoder;

import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ByteDecoderTest {
    ByteDecoder decoder = new ByteDecoder();

    @Test
    public void testDecode1() {
        byte[] datagram = {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(decoder.decode(datagram, 0), -2);
        assertEquals(decoder.decode(datagram, 1), -1);
        assertEquals(decoder.decode(datagram, 2), 0);
        assertEquals(decoder.decode(datagram, 4), 2);
        assertEquals(decoder.decode(datagram, 10), 8);
        assertEquals(decoder.decode(datagram, 11), 9);
        assertEquals(decoder.decode(datagram, 11 - datagram.length), 9);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 2));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -101));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10));

    }
}
