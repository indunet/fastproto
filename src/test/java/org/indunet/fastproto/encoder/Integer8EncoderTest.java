package org.indunet.fastproto.encoder;

import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        encoder.encode(datagram, 1 - datagram.length, -52);

        assertEquals(datagram[0], 10);
        assertEquals(datagram[1], -52);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, 8));

        assertThrows(EncodeException.class, () -> encoder.encode(datagram, -101, 1));
        assertThrows(EncodeException.class, () -> encoder.encode(datagram, 10, 255));
    }
}