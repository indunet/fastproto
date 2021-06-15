/*
 * Copyright 2019-2021 indunet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    protected static final int DECODE_NUM = 10;     // 1800 per second
    protected static final int ENCODE_NUM = 10;     // 3000 per second

    @ParameterizedTest
    @MethodSource
    public void testDecode(byte[] datagram) {
        FastProto.parseFrom(datagram, Batches.class);
    }

    public static List<Arguments> testDecode() {
        val random = new Random(System.currentTimeMillis());

        return IntStream.range(0, DECODE_NUM)
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
        byte[] datagram = FastProto.toByteArray(batches, 128);
    }

    public static List<Arguments> testEncode() {
        val random = new Random(System.currentTimeMillis());

        return IntStream.range(0, ENCODE_NUM)
                .mapToObj(__ -> {
                    byte[] d = new byte[128];

                    IntStream.range(0, d.length)
                            .forEach(i -> d[i] = (byte) random.nextInt());

                    return d;
                }).map(d -> FastProto.parseFrom(d, Batches.class))
                .map(Arguments::arguments)
                .collect(Collectors.toList());
    }
}
