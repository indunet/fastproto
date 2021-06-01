package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class Integer16DecoderTest {
    @Test
    public void testDecode() {
        byte[] datagram = new byte[10];

        // Value 1
        datagram[0] = 1;
        datagram[1] = 2;

        // Value 2
        short value = -29;
        datagram[2] |= (value >> 8);
        datagram[3] |= (value & 0xFF);

        Integer16Decoder decoder = new Integer16Decoder();

        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE), 1 + 256 * 2);
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.BIG), 256 + 2);

        assertEquals(decoder.decode(datagram, 2, EndianPolicy.BIG), -29);
    }
}