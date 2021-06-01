package org.indunet.fastproto.encoder;

import org.indunet.fastproto.exception.EncodeException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class UInteger8EncoderTest {
    UInteger8Encoder encoder = new UInteger8Encoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[2];

        encoder.encode(datagram, 0, 1);
        encoder.encode(datagram, 1, 255);

        byte[] cache = new byte[]{1, -1};

        assertArrayEquals(datagram, cache);
    }

    @Test(expected = EncodeException.class)
    public void testEncode2() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 10, 1);
    }

    @Test(expected = EncodeException.class)
    public void testEncode3() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 0, 256);
    }
}