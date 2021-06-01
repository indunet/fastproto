package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShortDecoderTest {
    ShortDecoder decoder = new ShortDecoder();

    @Test
    public void testDecode1() {
        byte[] datagram = {0, 1, 2, 3, -1, -1, -2, -1};

        // For little endian.
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE), 256);
        assertEquals(decoder.decode(datagram, 2, EndianPolicy.LITTLE), 2 + 3 * 256);
        assertEquals(decoder.decode(datagram, 4, EndianPolicy.LITTLE), -1);
        assertEquals(decoder.decode(datagram, 6, EndianPolicy.LITTLE), -2);

        // For big endian.
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.BIG), 0x0001);
        assertEquals(decoder.decode(datagram, 2, EndianPolicy.BIG), 0x0203);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, EndianPolicy.LITTLE));
    }
}
