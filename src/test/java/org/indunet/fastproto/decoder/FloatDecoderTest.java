package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FloatDecoderTest {
    FloatDecoder decoder = new FloatDecoder();
    float pi = 3.141f, e = 2.718f;

    @Test
    public void testDecode1() {
        assertEquals(decoder.decode(BinaryUtils.valueOf(pi), 0, EndianPolicy.LITTLE), pi, 0.0001);
        assertEquals(decoder.decode(BinaryUtils.valueOf(e), 0, EndianPolicy.LITTLE), e, 0.0001);

        assertEquals(decoder.decode(BinaryUtils.valueOf(e, EndianPolicy.BIG), 0, EndianPolicy.BIG), e, 0.0001);
        assertEquals(decoder.decode(BinaryUtils.valueOf(e, EndianPolicy.BIG), -4, EndianPolicy.BIG), e, 0.0001);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 0, EndianPolicy.LITTLE));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -1, EndianPolicy.BIG));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 8, EndianPolicy.BIG));
    }
}
