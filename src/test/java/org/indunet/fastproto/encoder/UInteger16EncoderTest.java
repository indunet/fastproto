package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        encoder.encode(datagram, 2 - datagram.length, EndianPolicy.BIG, 0x0304);

        byte[] cache = new byte[]{0x02, 0x01, 0x03, 0x04};
        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(EncodeException.class,
                () -> encoder.encode(datagram, 10, EndianPolicy.LITTLE, 1));
        assertThrows(EncodeException.class,
                () -> encoder.encode(datagram, 10, EndianPolicy.LITTLE, Integer.MAX_VALUE));
    }
}