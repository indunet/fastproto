package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @since 1.5.0
 */
class UInteger64DecoderTest {
    UInteger64Decoder decoder = new UInteger64Decoder();

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, EndianPolicy policy, BigInteger value) {
        assertEquals(decoder.decode(datagram, 0, policy), value);
        assertEquals(decoder.decode(datagram, -8, policy), value);
    }

    public static List<Arguments> testDecode1() {
        return Stream.of(
                Arguments.arguments(new byte[] {0, 0, 0, 0, 1, 0, 0, 0}, EndianPolicy.LITTLE, new BigInteger(String.valueOf((long) Math.pow(256, 4)))),
                Arguments.arguments(new byte[] {0, 0, 0, 0, 1, 0, 0, 1}, EndianPolicy.LITTLE, new BigInteger(String.valueOf((long) Math.pow(256, 4) + (long) Math.pow(256, 7)))),
                Arguments.arguments(new byte[] {0, 0, 0, 0, 1, 0, 1, 1}, EndianPolicy.BIG, new BigInteger(String.valueOf((long) Math.pow(256, 3) + 1 + 256)))
        ).collect(Collectors.toList());
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 0, EndianPolicy.LITTLE));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -1, EndianPolicy.LITTLE));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, EndianPolicy.LITTLE));
    }
}