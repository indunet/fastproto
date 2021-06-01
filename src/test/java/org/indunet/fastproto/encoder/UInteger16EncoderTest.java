package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class UInteger16EncoderTest {
    UInteger16Encoder encoder = new UInteger16Encoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[4];

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, 0x0102);
        encoder.encode(datagram, 2, EndianPolicy.BIG, 0x0304);

        byte[] cache = new byte[]{0x02, 0x01, 0x03, 0x04};
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

        encoder.encode(datagram, 10, EndianPolicy.LITTLE, Integer.MAX_VALUE);
    }
}