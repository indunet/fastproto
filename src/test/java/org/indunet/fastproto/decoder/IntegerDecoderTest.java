package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegerDecoderTest {
    IntegerDecoder decoder = new IntegerDecoder();
    private byte[] datagram = {0, 1, 0, 1, -1, -1, -1, -1};

    @Test
    public void testDecode1() {
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE), 256 + 256 * 256 * 256);
        assertEquals(decoder.decode(datagram, 4, EndianPolicy.LITTLE), -1);

        assertEquals(decoder.decode(datagram, 0, EndianPolicy.BIG), 0x00010001);
        assertEquals(decoder.decode(datagram, 4, EndianPolicy.BIG), -1);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, EndianPolicy.LITTLE));
    }
}
