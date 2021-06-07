package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DoubleDecoderTest {
    DoubleDecoder decoder = new DoubleDecoder();
    double pi = 3.141, e = 2.718;

    @Test
    public void testDecode1() {
        assertEquals(decoder.decode(NumberUtils.doubleToBinary(pi), 0, EndianPolicy.LITTLE), pi, 0.001);
        assertEquals(decoder.decode(NumberUtils.doubleToBinary(e), 0, EndianPolicy.LITTLE), e, 0.001);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class,
                () -> this.decoder.decode(null, 10, EndianPolicy.LITTLE));

        assertThrows(DecodeException.class,
                () -> this.decoder.decode(datagram, -1, EndianPolicy.LITTLE));
        assertThrows(DecodeException.class,
                () -> this.decoder.decode(datagram, 8, EndianPolicy.LITTLE));
    }
}
