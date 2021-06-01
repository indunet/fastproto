package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class UInteger16DecoderTest {
    @Test
    public void decode() {
        byte[] datagram = new byte[10];
        datagram[0] = 1;
        datagram[1] = 2;

        UInteger16Decoder decoder = new UInteger16Decoder();

        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE),   1 + 2 * 256);
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.BIG),  256 + 2);
    }
}