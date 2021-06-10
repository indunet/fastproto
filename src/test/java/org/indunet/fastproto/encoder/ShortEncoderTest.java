package org.indunet.fastproto.encoder;

import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
public class ShortEncoderTest {
    ShortEncoder encoder = new ShortEncoder();

    @Test
    public void testEncode1() {
        val datagram = new byte[4];

        this.encoder.encode(datagram, 0, EndianPolicy.BIG, (short )0x0102);
        this.encoder.encode(datagram, 2 - datagram.length, EndianPolicy.LITTLE, (short ) 0x0304);

        val cache = new byte[] {1, 2, 4, 3};
        assertArrayEquals(datagram, cache);
    }

    @Test
    public void testEncode2() {
        val datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, EndianPolicy.BIG, (short) 1));

        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, -1, EndianPolicy.BIG, (short) 0));
        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 10, EndianPolicy.LITTLE, (short) 0));
    }
}
