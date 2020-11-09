package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FloatDecoderTest {
    FloatDecoder decoder = new FloatDecoder();
    float pi = 3.141f, e = 2.718f;

    @Test
    public void testGet() {
        assertEquals(decoder.decode(NumberUtils.floatToBinary(pi), 0, Endian.Little), pi, 0.0001);
        assertEquals(decoder.decode(NumberUtils.floatToBinary(e), 0, Endian.Little), e, 0.0001);
    }
}
