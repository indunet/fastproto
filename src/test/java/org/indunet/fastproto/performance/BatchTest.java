package org.indunet.fastproto.performance;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Deng Ran
 * @since 1.4.0
 */
public class BatchTest {
    @ParameterizedTest
    @MethodSource
    public void testDecode(byte[] datagram) {
        FastProto.decode(datagram, Batches.class);
    }

    public static List<Arguments> testDecode() {
        val random = new Random(System.currentTimeMillis());

        return IntStream.range(0, 1800)
                .mapToObj(__ -> {
                    byte[] d = new byte[128];

                    IntStream.range(0, d.length)
                            .forEach(i -> d[i] = (byte) random.nextInt());

                    return d;
                }).map(Arguments::arguments)
                .collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testEncode(Batches batches) {
        byte[] datagram = FastProto.encode(batches, 128);
    }

    public static List<Arguments> testEncode() {
        val random = new Random(System.currentTimeMillis());

        return IntStream.range(0, 3000)
                .mapToObj(__ -> {
                    byte[] d = new byte[128];

                    IntStream.range(0, d.length)
                            .forEach(i -> d[i] = (byte) random.nextInt());

                    return d;
                }).map(d -> FastProto.decode(d, Batches.class))
                .map(Arguments::arguments)
                .collect(Collectors.toList());
    }
}
