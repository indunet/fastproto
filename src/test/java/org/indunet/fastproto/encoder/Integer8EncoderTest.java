package org.indunet.fastproto.encoder;

import org.indunet.fastproto.exception.EncodeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class Integer8EncoderTest {
    Integer8Encoder encoder = new Integer8Encoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 0, 10);
        encoder.encode(datagram, 1, -52);

        assertEquals(datagram[0], 10);
        assertEquals(datagram[1], -52);
    }

    @Test(expected = EncodeException.class)
    public void testEncode2() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 10, 1);
    }

    @Test(expected = EncodeException.class)
    public void testEncode3() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 10, 255);
    }
}