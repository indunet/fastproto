package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Deng Ran
 * @since 1.1.0
 */
public class CharacterEncoderTest {
    @Test
    public void encode() {
        CharacterEncoder encoder = new CharacterEncoder();
        byte[] datagram = new byte[10];

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, 'A');

        byte[] cache = new byte[10];
        cache[0] = 65;
        assertArrayEquals(cache, datagram);
    }
}