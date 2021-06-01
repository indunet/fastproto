package org.indunet.fastproto.decoder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class UInteger8DecoderTest {
    @Test
    public void testDecode() {
        byte[] datagram = new byte[10];
        datagram[0] = -10;
        datagram[1] = 29;

        UInteger8Decoder decoder = new UInteger8Decoder();

        assertEquals(decoder.decode(datagram, 0), 256 - 10);
        assertEquals(decoder.decode(datagram, 1), 29);
    }
}
