package org.indunet.fastproto.decoder;

import org.junit.Test;
import org.vnet.fastproto.exception.DecodeException;
import org.vnet.fastproto.utils.NumberUtils;

public class FloatDecoderTest {
    NumberDecoder<?> decoder = new StandardFloatDecoder();
    Float pi = 3.141f, e = 2.718f;

    @Test
    public void testGet() throws DecodeException {
        assertEquals(decoder.get(NumberUtils.floatToByteArray(pi), 0), pi);
        assertEquals(decoder.get(NumberUtils.floatToByteArray(e), 0), e);
    }
}
