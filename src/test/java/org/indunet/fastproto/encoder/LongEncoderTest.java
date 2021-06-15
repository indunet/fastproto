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

package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class LongEncoderTest {
    LongEncoder encoder = new LongEncoder();

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, EndianPolicy policy, long value, byte[] expected) {
        encoder.encode(datagram, byteOffset, policy, value);

        assertArrayEquals(expected, datagram);
    }

    public static List<Arguments> testEncode1() {
        return Stream.of(
                Arguments.arguments(new byte[8], 0, EndianPolicy.LITTLE, -101L, BinaryUtils.valueOf(-101L)),
                Arguments.arguments(new byte[8], -8, EndianPolicy.LITTLE, -101L, BinaryUtils.valueOf(-101L)),
                Arguments.arguments(new byte[8], 0, EndianPolicy.BIG, (long) Integer.MAX_VALUE,
                        BinaryUtils.valueOf((long) Integer.MAX_VALUE, EndianPolicy.BIG))
        ).collect(Collectors.toList());
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, EndianPolicy.BIG, 8));

        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, -1, EndianPolicy.LITTLE, -1L));
        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, 8, EndianPolicy.LITTLE, -1L));
    }
}
