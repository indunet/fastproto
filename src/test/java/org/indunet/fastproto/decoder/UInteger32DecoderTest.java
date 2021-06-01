package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodeException;
import org.junit.Test;

import static org.junit.Assert.*;

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

        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE), 1 + 2 * 256l * 256 * 256);
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.BIG), 256 * 256 * 256 + 2);
    }

    @Test(expected = DecodeException.class)
    public void testDecode2() {
        byte[] datagram = new byte[10];

        decoder.decode(datagram, 8, EndianPolicy.LITTLE);
    }
}