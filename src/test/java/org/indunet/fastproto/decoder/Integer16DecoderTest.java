package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class Integer16DecoderTest {
    Integer16Decoder decoder = new Integer16Decoder();

    @Test
    public void testDecode1() {
        byte[] datagram = new byte[10];

        // Value 1
        datagram[0] = 1;
        datagram[1] = 2;

        // Value 2
        short value = -29;
        datagram[2] |= (value >> 8);
        datagram[3] |= (value & 0xFF);

        assertEquals(this.decoder.decode(datagram, 0, EndianPolicy.LITTLE), 1 + 256 * 2);
        assertEquals(this.decoder.decode(datagram, 0, EndianPolicy.BIG), 256 + 2);
        assertEquals(this.decoder.decode(datagram, 2, EndianPolicy.BIG), -29);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 0, EndianPolicy.LITTLE));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -1, EndianPolicy.LITTLE));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, EndianPolicy.LITTLE));
    }
}