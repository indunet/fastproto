package org.indunet.fastproto.decoder;

import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class StringDecoderTest {
    @Test
    public void testDecode() {
        StringDecoder decoder = new StringDecoder();

        assertEquals(decoder.decode(
                "ABCabc".getBytes(), 0, -1, Charset.forName("utf-8")), "ABCabc");
    }
}
