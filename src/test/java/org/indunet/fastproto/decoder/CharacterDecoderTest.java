package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Deng Ran
 * @version 1.1.0
 */
public class CharacterDecoderTest {
    @Test
    public void decode() {
        CharacterDecoder decoder = new CharacterDecoder();
        byte[] datagram = new byte[10];
        datagram[0] = 'a';
        datagram[2] = 'A';

        assertEquals('a', (char) decoder.decode(datagram, 0, EndianPolicy.LITTLE));
        assertEquals('A', (char) decoder.decode(datagram, 2, EndianPolicy.LITTLE));
    }
}