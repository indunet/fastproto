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

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, EndianPolicy policy, long value) {
        assertEquals(decoder.decode(datagram, 0, policy), value);
        assertEquals(decoder.decode(datagram, -8, policy), value);
    }

    public static List<Arguments> testDecode1() {
        return Stream.of(
                Arguments.arguments(new byte[] {-1, -1, -1, -1, -1, -1, -1, -1}, EndianPolicy.LITTLE, -1L),
                Arguments.arguments(new byte[] {0, 0, 0, 0, 1, 0, 0, 1}, EndianPolicy.LITTLE, (long) Math.pow(256, 4) + (long) Math.pow(256, 7)),
                Arguments.arguments(new byte[] {0, 0, 0, 0, 1, 0, 0, 1}, EndianPolicy.BIG, (long) Math.pow(256, 3) + 1)
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
