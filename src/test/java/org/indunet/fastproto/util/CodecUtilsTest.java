package org.indunet.fastproto.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of codec utils.
 *
 * @author Deng Ran
 * @since 3.9.2
 */
public class CodecUtilsTest {
    @Test
    public void testEncodeByte() {
        byte[] datagram = new byte[10];

        CodecUtils.byteType(datagram, 0, (byte) 1);
        CodecUtils.byteType(datagram, 1 - datagram.length, (byte) -128);

        assertEquals(datagram[0], 1);
        assertEquals(datagram[1], -128);
    }
}
