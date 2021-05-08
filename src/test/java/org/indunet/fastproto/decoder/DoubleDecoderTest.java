package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DoubleDecoderTest {
    DoubleDecoder decoder = new DoubleDecoder();
    double pi = 3.141, e = 2.718;

    @Test
    public void testDecode() {
         assertEquals(decoder.decode(NumberUtils.doubleToBinary(pi), 0, EndianPolicy.Little), pi, 0.001);
         assertEquals(decoder.decode(NumberUtils.doubleToBinary(e), 0, EndianPolicy.Little), e, 0.001);
    }
}
