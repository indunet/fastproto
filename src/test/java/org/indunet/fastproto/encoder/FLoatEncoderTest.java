package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class FLoatEncoderTest {
    FloatEncoder encoder = new FloatEncoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[4];
        float pi = 3.141f, e = 2.718f;

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, pi);
        assertArrayEquals(datagram, NumberUtils.floatToBinary(pi));

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, e);
        assertArrayEquals(datagram, NumberUtils.floatToBinary(e));
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, null, 3.14f));

        assertThrows(EncodeException.class, () ->
                this.encoder.encode(datagram, 10, EndianPolicy.LITTLE, 3.141f));
        assertThrows(EncodeException.class, () ->
                this.encoder.encode(datagram, -1, EndianPolicy.LITTLE, 3.141f));
    }
}
