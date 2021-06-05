package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LongDecoderTest {
    LongDecoder decoder = new LongDecoder();
    private byte[] datagram;

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, long value) {
        assertEquals(decoder.decode(datagram, 0, EndianPolicy.LITTLE), value);
    }

    public static List<Arguments> testDecode1() {
        return Stream.of(
                Arguments.arguments(new byte[] {-1, -1, -1, -1, -1, -1, -1, -1}, -1L),
                Arguments.arguments(new byte[] {0, 0, 0, 0, 1, 0, 0, 1}, (long) Math.pow(256, 4) + (long) Math.pow(256, 7))
        ).collect(Collectors.toList());
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, EndianPolicy.LITTLE));
    }
}
