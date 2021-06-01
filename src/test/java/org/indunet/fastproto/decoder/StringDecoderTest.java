package org.indunet.fastproto.decoder;

import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class StringDecoderTest {
    StringDecoder decoder = new StringDecoder();

    @Test
    public void testDecode1() {
        assertEquals(decoder.decode(
                "ABCabc".getBytes(), 0, -1, StandardCharsets.UTF_8), "ABCabc");
    }

    @Test
    public void testDecode2() {
        DecodeException decodeException = assertThrows(DecodeException.class,
                () -> this.decoder.decode("ABCabc".getBytes(), 6, 10, StandardCharsets.UTF_8));
    }
}
