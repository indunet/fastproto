package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class TimestampDecoderTest {
    TimestampDecoder decoder = new TimestampDecoder();

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, int byteOffset, ProtocolType type, EndianPolicy policy, TimeUnit unit, Timestamp expected) {
        Timestamp value = decoder.decode(datagram, byteOffset, type, policy, unit);

        assertEquals(expected, value);
    }

    public static List<Arguments> testDecode1() {
        long current = System.currentTimeMillis();

        return Stream.of(
                Arguments.arguments(BinaryUtils.valueOf(current), 0, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Timestamp(current)),
                Arguments.arguments(BinaryUtils.uint32of(current / 1000), 0, ProtocolType.UINTEGER32, EndianPolicy.LITTLE, TimeUnit.SECONDS, new Timestamp(current / 1000 * 1000)),
                Arguments.arguments(BinaryUtils.uint32of(current / 1000), -4, ProtocolType.UINTEGER32, EndianPolicy.LITTLE, TimeUnit.SECONDS, new Timestamp(current / 1000 * 1000))
            ).collect(Collectors.toList());
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 0, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -1, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS));
    }
}