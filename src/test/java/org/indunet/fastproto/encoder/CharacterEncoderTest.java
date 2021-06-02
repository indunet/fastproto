package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.1.0
 */
public class CharacterEncoderTest {
    CharacterEncoder encoder = new CharacterEncoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, 'A');
        encoder.encode(datagram, 2, EndianPolicy.BIG, 'a');

        byte[] cache = new byte[10];
        cache[0] = 65;
        cache[3] = 97;
        assertArrayEquals(cache, datagram);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 10, EndianPolicy.LITTLE, 'A'));
    }
}