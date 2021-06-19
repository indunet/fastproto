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
                Arguments.arguments("abcdef".getBytes(), 3, 3, StandardCharsets.UTF_8, "def"),
                Arguments.arguments("abcdef".getBytes(), -3, 3, StandardCharsets.UTF_8, "def")
        ).collect(Collectors.toList());
    }

    @Test
    public void testDecode2() {
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 2, 1,StandardCharsets.UTF_8));

        assertThrows(DecodeException.class, () -> this.decoder.decode("ABCabc".getBytes(), -1, 10, StandardCharsets.UTF_8));
        assertThrows(DecodeException.class, () -> this.decoder.decode("ABCabc".getBytes(), 5, -2, StandardCharsets.UTF_8));
        assertThrows(DecodeException.class, () -> this.decoder.decode("ABCabc".getBytes(), 0, 10, StandardCharsets.UTF_8));
        assertThrows(DecodeException.class, () -> this.decoder.decode("ABCabc".getBytes(), 10, -1, StandardCharsets.UTF_8));
    }
}
