package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.util.BinaryUtils;
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
class UInteger64EncoderTest {
    UInteger64Encoder encoder = new UInteger64Encoder();

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, EndianPolicy policy, BigInteger value, byte[] expected) {
        encoder.encode(datagram, byteOffset, policy, value);

        assertArrayEquals(expected, datagram);
    }

    public static List<Arguments> testEncode1() {
        return Stream.of(
                Arguments.arguments(new byte[8], 0, EndianPolicy.LITTLE, new BigInteger(String.valueOf(101L)), BinaryUtils.valueOf(101L)),
                Arguments.arguments(new byte[8], -8, EndianPolicy.LITTLE, new BigInteger(String.valueOf(101L)), BinaryUtils.valueOf(101L)),
                Arguments.arguments(new byte[8], 0, EndianPolicy.LITTLE, new BigInteger(String.valueOf(Long.MAX_VALUE)), BinaryUtils.valueOf(Long.MAX_VALUE)),
                Arguments.arguments(new byte[8], 0, EndianPolicy.BIG, new BigInteger(String.valueOf(Integer.MAX_VALUE)),
                        BinaryUtils.valueOf((long) Integer.MAX_VALUE, EndianPolicy.BIG))
        ).collect(Collectors.toList());
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, EndianPolicy.BIG, new BigInteger("8")));

        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, -1, EndianPolicy.LITTLE, new BigInteger("-1")));
        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, 8, EndianPolicy.LITTLE, new BigInteger("0")));
    }
}