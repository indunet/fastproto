package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DoubleDecoderTest {
    DoubleDecoder decoder = new DoubleDecoder();
    double pi = 3.141, e = 2.718;

    @Test
    public void testDecode1() {
        assertEquals(decoder.decode(BinaryUtils.valueOf(pi), 0, EndianPolicy.LITTLE), pi, 0.001);
        assertEquals(decoder.decode(BinaryUtils.valueOf(e), 0, EndianPolicy.LITTLE), e, 0.001);

        assertEquals(decoder.decode(BinaryUtils.valueOf(pi, EndianPolicy.BIG), 0, EndianPolicy.BIG), pi, 0.001);
        assertEquals(decoder.decode(BinaryUtils.valueOf(pi, EndianPolicy.BIG), -8, EndianPolicy.BIG), pi, 0.001);
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
