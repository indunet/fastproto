package org.indunet.fastproto.encoder;

import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        encoder.encode(datagram, 1 - datagram.length, 255);

        byte[] cache = new byte[]{1, -1};

        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, 8));

        assertThrows(EncodeException.class,
                () -> encoder.encode(datagram, -101, 1));
        assertThrows(EncodeException.class,
                () -> encoder.encode(datagram, 10, 1));
        assertThrows(EncodeException.class,
                () -> encoder.encode(datagram, 0, 256));
    }
}