package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @version 1.1.0
 */
public class CharacterDecoderTest {
    CharacterDecoder decoder = new CharacterDecoder();

    @Test
    public void testDecode1() {
        byte[] datagram = new byte[10];
        datagram[0] = 'a';
        datagram[2] = 'A';

        assertEquals('a', (char) decoder.decode(datagram, 0, EndianPolicy.LITTLE));
        assertEquals('A', (char) decoder.decode(datagram, 2, EndianPolicy.LITTLE));
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 10, EndianPolicy.BIG));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -1, EndianPolicy.BIG));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, EndianPolicy.LITTLE));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 9, EndianPolicy.LITTLE));
    }
}