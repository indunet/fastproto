package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
public class Integer16EncoderTest {
    Integer16Encoder encoder = new Integer16Encoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[4];

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, 0x0102);
        encoder.encode(datagram, 2, EndianPolicy.BIG, -300);

        byte[] cache = new byte[4];
        cache[0] = 2;
        cache[1] = 1;
        cache[2] |= ((short) -300 >> 8);
        cache[3] |= ((short) -300 & 0xFF);

        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, EndianPolicy.BIG, 8));

        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, -1, EndianPolicy.LITTLE, 1));
        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, 10, EndianPolicy.LITTLE, 1));
        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, 10, EndianPolicy.LITTLE, Integer.MAX_VALUE));
    }
}