package org.indunet.fastproto.encoder;

import org.indunet.fastproto.exception.EncodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class BinaryEncoderTest {
    BinaryEncoder encoder = new BinaryEncoder();

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, int length, byte[] bytes, byte[] actual) {
        encoder.encode(datagram, byteOffset, length, bytes);
        assertArrayEquals(datagram, actual);
    }

    public static List<Arguments> testEncode1() {
        return Stream.of(
                Arguments.arguments(new byte[4], 0, -1, new byte[] {1, 2, 3, 4}, new byte[] {1, 2, 3, 4}),
                Arguments.arguments(new byte[6], 2, 3, new byte[] {1, 2, 3, 4}, new byte[] {0, 0, 1, 2, 3, 0}),
                Arguments.arguments(new byte[8], 4, -1, new byte[] {1, 2, 3, 4}, new byte[] {0, 0, 0, 0, 1, 2, 3, 4}),
                Arguments.arguments(new byte[8], -4, -1, new byte[] {1, 2, 3, 4}, new byte[] {0, 0, 0, 0, 1, 2, 3, 4})
        ).collect(Collectors.toList());
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, -1, new byte[8]));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(datagram, 0, -1, null));

        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, -2, -1, new byte[8]));
        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 10, -1, new byte[8]));
        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 0, -2, new byte[8]));
        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 8, -1, new byte[8]));
    }
}
