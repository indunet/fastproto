package org.indunet.fastproto.encoder;

import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class StringEncoderTest {
    @Test
    public void testEncode() {
        byte[] datagram = new byte[6];
        String value = "abcABC";
        StringEncoder encoder = new StringEncoder();

        encoder.encode(datagram, 0, -1, Charset.defaultCharset(), value);
        assertEquals(value, new String(datagram));
    }
}
