package org.indunet.fastproto.encoder;

import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class BinaryEncoderTest {
    BinaryEncoder encoder = new BinaryEncoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[4];

        encoder.encode(datagram, 0, new byte[] {0x01, 0x02, 0x03, 0x04});
        assertArrayEquals(datagram, new byte[] {0x01, 0x02, 0x03, 0x04});
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 10, new byte[8]));
    }
}
