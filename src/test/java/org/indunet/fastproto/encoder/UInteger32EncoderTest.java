package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class UInteger32EncoderTest {
    UInteger32Encoder encoder = new UInteger32Encoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[8];

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, 0x01020304);
        encoder.encode(datagram, 4, EndianPolicy.BIG, 0x05060708);

        byte[] cache = new byte[]{0x04, 0x03, 0x02, 0x01, 0x05, 0x06, 0x07, 0x08};
        assertArrayEquals(datagram, cache);
    }

    @Test(expected = EncodeException.class)
    public void testEncode2() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 10, EndianPolicy.LITTLE, 1);
    }

    @Test(expected = EncodeException.class)
    public void testEncode3() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, Long.MAX_VALUE);
    }
}