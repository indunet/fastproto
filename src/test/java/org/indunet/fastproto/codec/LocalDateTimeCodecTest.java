package org.indunet.fastproto.codec;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test of LocalDateTime type codec.
 *
 * @author Deng Ran
 * @since 3.3.1
 */
public class LocalDateTimeCodecTest {
    LocalDateTimeCodec codec = new LocalDateTimeCodec();

    public static List<Arguments> testDecode1() {
        long current = System.currentTimeMillis();

        return Stream.of(
                Arguments.arguments(BinaryUtils.valueOf(current), 0, EndianPolicy.LITTLE,
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(current), ZoneId.systemDefault())),
                Arguments.arguments(BinaryUtils.valueOf(current, EndianPolicy.BIG), 0, EndianPolicy.BIG,
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(current), ZoneId.systemDefault()))
        ).collect(Collectors.toList());
    }

    public static List<Arguments> testEncode1() {
        long current = System.currentTimeMillis();

        return Stream.of(
                Arguments.arguments(new byte[8], 0, EndianPolicy.LITTLE,
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(current), ZoneId.systemDefault()), BinaryUtils.valueOf(current)),
                Arguments.arguments(new byte[8], -8, EndianPolicy.BIG,
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(current), ZoneId.systemDefault()), BinaryUtils.valueOf(current, EndianPolicy.BIG))
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, int byteOffset, EndianPolicy policy, LocalDateTime expected) {
        assertEquals(expected, codec.decode(datagram, byteOffset, policy));
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 0, EndianPolicy.LITTLE));

        assertThrows(DecodingException.class, () -> this.codec.decode(datagram, 10, EndianPolicy.LITTLE));
    }

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, EndianPolicy policy, LocalDateTime value, byte[] expected) {
        this.codec.encode(datagram, byteOffset, policy, value);

        assertArrayEquals(datagram, expected);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.encode(null, 0, EndianPolicy.LITTLE,
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault())));
        assertThrows(NullPointerException.class,
                () -> this.codec.encode(null, 0, EndianPolicy.LITTLE, null));

        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, -1, EndianPolicy.LITTLE,
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault())));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 10, EndianPolicy.LITTLE,
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault())));
    }
}
