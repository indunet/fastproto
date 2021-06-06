package org.indunet.fastproto.decoder;

import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class UInteger8DecoderTest {
    UInteger8Decoder decoder = new UInteger8Decoder();

    @Test
    public void testDecode1() {
        byte[] datagram = new byte[10];
        datagram[0] = -10;
        datagram[1] = 29;

        assertEquals(decoder.decode(datagram, 0), 256 - 10);
        assertEquals(decoder.decode(datagram, 1), 29);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -1));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10));
    }
}
