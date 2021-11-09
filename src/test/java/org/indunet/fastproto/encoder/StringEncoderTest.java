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

import org.indunet.fastproto.exception.EncodingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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


        assertThrows(EncodingException.class, () -> this.encoder.encode(datagram, -1, 2, StandardCharsets.UTF_8, "ABC"));
        assertThrows(EncodingException.class, () -> this.encoder.encode(datagram, 10, -1, StandardCharsets.UTF_8, "ABC"));
        assertThrows(EncodingException.class, () -> this.encoder.encode(datagram, 2, 10, StandardCharsets.UTF_8, "ABC"));
    }
}
