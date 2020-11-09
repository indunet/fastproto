package org.indunet.fastproto.decoder;

import org.junit.Test;
import org.vnet.fastproto.exception.DecodeException;
import org.vnet.fastproto.utils.NumberUtils;

public class DoubleDecoderTest {
    NumberDecoder<?> decoder = new StandardDoubleDecoder();
    double pi = 3.141, e = 2.718;

    @Test
    public void testGet() throws DecodeException {
        assertEquals(decoder.get(NumberUtils.doubleToByteArray(pi), 0), pi);
        assertEquals(decoder.get(NumberUtils.doubleToByteArray(e), 0), e);
    }
}
