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
                Arguments.arguments(new byte[4], 0, -1, new byte[]{1, 2, 3, 4}, new byte[]{1, 2, 3, 4}),
                Arguments.arguments(new byte[6], 2, 3, new byte[]{1, 2, 3, 4}, new byte[]{0, 0, 1, 2, 3, 0}),
                Arguments.arguments(new byte[8], 4, -1, new byte[]{1, 2, 3, 4}, new byte[]{0, 0, 0, 0, 1, 2, 3, 4}),
                Arguments.arguments(new byte[8], -4, -1, new byte[]{1, 2, 3, 4}, new byte[]{0, 0, 0, 0, 1, 2, 3, 4})
        ).collect(Collectors.toList());
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, -1, new byte[8]));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(datagram, 0, -1, null));

        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, -2, -7, new byte[8]));
        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, -11, -7, new byte[8]));
        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 10, -1, new byte[8]));
        assertThrows(EncodeException.class, () -> this.encoder.encode(datagram, 0, 11, new byte[8]));
    }
}
