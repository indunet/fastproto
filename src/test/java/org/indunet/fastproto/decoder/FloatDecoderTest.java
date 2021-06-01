package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FloatDecoderTest {
    FloatDecoder decoder = new FloatDecoder();
    float pi = 3.141f, e = 2.718f;

    @Test
    public void testGet() {
        assertEquals(decoder.decode(NumberUtils.floatToBinary(pi), 0, EndianPolicy.LITTLE), pi, 0.0001);
        assertEquals(decoder.decode(NumberUtils.floatToBinary(e), 0, EndianPolicy.LITTLE), e, 0.0001);
    }
}
