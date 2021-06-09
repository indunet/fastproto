package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class DoubleEncoderTest {
    DoubleEncoder encoder = new DoubleEncoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[8];
        double pi = 3.141, e = 2.718;

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, e);
        assertArrayEquals(datagram, BinaryUtils.valueOf(e));

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, pi);
        assertArrayEquals(datagram, BinaryUtils.valueOf(pi));

        encoder.encode(datagram, 0 - datagram.length, EndianPolicy.BIG, pi);
        assertArrayEquals(datagram, BinaryUtils.valueOf(pi, EndianPolicy.BIG));
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, null, 3.14));

        assertThrows(EncodeException.class, () ->
                this.encoder.encode(datagram, -1, EndianPolicy.LITTLE, 3.141));
        assertThrows(EncodeException.class, () ->
                this.encoder.encode(datagram, 10, EndianPolicy.LITTLE, 3.141));
    }
}
