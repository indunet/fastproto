package org.indunet.fastproto.encoder;

import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class ByteEncoderTest {
    ByteEncoder encoder = new ByteEncoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 0, (byte) 1);
        encoder.encode(datagram, 1, (byte) -128);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 10, (byte) 1));
    }
}
