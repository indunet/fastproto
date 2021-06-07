package org.indunet.fastproto.encoder;


import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class BooleanEncoderTest {
    BooleanEncoder encoder = new BooleanEncoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 0, 0, true);
        encoder.encode(datagram, 1, 7, true);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);

        encoder.encode(datagram, 0, 0, false);
        encoder.encode(datagram, 1, 7, false);

        assertEquals(datagram[0], 0);
        assertEquals(datagram[1], 0);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, -1, true));

        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, -1, 1, true));
        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, 10, 1, true));
    }
}
