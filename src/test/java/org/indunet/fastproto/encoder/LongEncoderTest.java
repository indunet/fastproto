package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class LongEncoderTest {
    LongEncoder encoder = new LongEncoder();
    byte[] datagram = new byte[16];

    @Test
    public void testEncode1() {
        encoder.encode(datagram, 0, EndianPolicy.LITTLE, 0x0102030405060708L);
        encoder.encode(datagram, 8, EndianPolicy.LITTLE, -1L);

        assertArrayEquals(datagram, new byte[]{0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01,
                -1, -1, -1, -1, -1, -1, -1, -1});
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, EndianPolicy.BIG, 8));

        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, -1, EndianPolicy.LITTLE, -1L));
        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, 8, EndianPolicy.LITTLE, -1L));
    }
}
