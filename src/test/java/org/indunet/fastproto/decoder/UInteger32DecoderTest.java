package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class UInteger32DecoderTest {
    UInteger32Decoder decoder = new UInteger32Decoder();

    @Test
    public void testDecode1() {
        byte[] datagram = new byte[10];
        datagram[0] = 1;
        datagram[1] = 0;
        datagram[2] = 0;
        datagram[3] = 2;

        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE), 1 + 2 * 256L * 256 * 256);
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.BIG), 256 * 256 * 256 + 2);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -1, EndianPolicy.LITTLE));
        assertThrows(DecodeException.class, () -> decoder.decode(datagram, 8, EndianPolicy.LITTLE));
    }
}