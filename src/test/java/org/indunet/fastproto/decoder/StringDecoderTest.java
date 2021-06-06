package org.indunet.fastproto.decoder;

import org.indunet.fastproto.exception.DecodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @version 1.0.0
 */
public class StringDecoderTest {
    StringDecoder decoder = new StringDecoder();

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, int byteOffset, int length, Charset set, String actual) {
        assertEquals(decoder.decode(datagram, byteOffset, length, set), actual);
    }

    public static List<Arguments> testDecode1() {
        return Stream.of(
                Arguments.arguments("ABCabc".getBytes(), 0, -1, StandardCharsets.UTF_8, "ABCabc"),
                Arguments.arguments("abcdef".getBytes(), 3, 3, StandardCharsets.UTF_8, "def")
        ).collect(Collectors.toList());
    }

    @Test
    public void testDecode2() {
        assertThrows(DecodeException.class, () -> this.decoder.decode("ABCabc".getBytes(), -1, 10, StandardCharsets.UTF_8));
        assertThrows(DecodeException.class, () -> this.decoder.decode("ABCabc".getBytes(), 0, -2, StandardCharsets.UTF_8));
        assertThrows(DecodeException.class, () -> this.decoder.decode("ABCabc".getBytes(), 0, 10, StandardCharsets.UTF_8));
        assertThrows(DecodeException.class, () -> this.decoder.decode("ABCabc".getBytes(), 10, -1, StandardCharsets.UTF_8));
    }
}
