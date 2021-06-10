package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class StringEncoderTest {
    StringEncoder encoder = new StringEncoder();

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, int length, Charset set, String value, byte[] actual) {
        encoder.encode(datagram, byteOffset, length, Charset.defaultCharset(), value);
        assertArrayEquals(datagram, actual);
    }

    public static List<Arguments> testEncode1() {
        return Stream.of(
                Arguments.arguments(new byte[6], 0, -1, StandardCharsets.UTF_8, "ABCabc", "ABCabc".getBytes()),
                Arguments.arguments(new byte[6], -6, -1, StandardCharsets.UTF_8, "ABCabc", "ABCabc".getBytes()),
                Arguments.arguments(new byte[8], 2, 6, StandardCharsets.UTF_8, "ABCabc", new byte[]{0, 0, 'A', 'B', 'C', 'a', 'b', 'c'})
        ).collect(Collectors.toList());
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, 8, StandardCharsets.UTF_8, "ABC"));


        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, -1, 2, StandardCharsets.UTF_8, "ABC"));
        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 0, -2, StandardCharsets.UTF_8, "ABC"));
        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 10, -1, StandardCharsets.UTF_8, "ABC"));
        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 2, 10, StandardCharsets.UTF_8, "ABC"));
    }
}
