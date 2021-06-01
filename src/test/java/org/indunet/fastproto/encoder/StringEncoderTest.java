package org.indunet.fastproto.encoder;

import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
