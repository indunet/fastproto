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
public class UInteger16DecoderTest {
    UInteger16Decoder decoder = new UInteger16Decoder();

    @Test
    public void testDecode1() {
        byte[] datagram = new byte[10];
        datagram[0] = 1;
        datagram[1] = 2;

        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE), 1 + 2 * 256);
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.BIG), 256 + 2);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, EndianPolicy.LITTLE));
    }
}