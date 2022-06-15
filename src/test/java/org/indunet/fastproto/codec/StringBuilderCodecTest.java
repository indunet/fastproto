/*
 * Copyright 2019-2022 indunet.org
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

package org.indunet.fastproto.codec;

import org.indunet.fastproto.exception.DecodingException;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of StringBuilder type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class StringBuilderCodecTest {
    StringBuilderCodec codec = new StringBuilderCodec();

    public static List<Arguments> testDecode1() {
        return Stream.of(
                Arguments.arguments("ABCabc".getBytes(), 0, -1, StandardCharsets.UTF_8, new StringBuilder("ABCabc")),
                Arguments.arguments("abcdef".getBytes(), 3, 3, StandardCharsets.UTF_8, new StringBuilder("def")),
                Arguments.arguments("abcdef".getBytes(), -3, 3, StandardCharsets.UTF_8, new StringBuilder("def"))
        ).collect(Collectors.toList());
    }

    public static List<Arguments> testEncode1() {
        return Stream.of(
                Arguments.arguments(new byte[6], 0, -1, StandardCharsets.UTF_8, new StringBuilder("ABCabc"), "ABCabc".getBytes()),
                Arguments.arguments(new byte[6], -6, -1, StandardCharsets.UTF_8, new StringBuilder("ABCabc"), "ABCabc".getBytes()),
                Arguments.arguments(new byte[8], 2, 6, StandardCharsets.UTF_8, new StringBuilder("ABCabc"), new byte[]{0, 0, 'A', 'B', 'C', 'a', 'b', 'c'})
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, int byteOffset, int length, Charset set, StringBuilder actual) {
        assertEquals(codec.decode(datagram, byteOffset, length, set).toString(), actual.toString());
    }

    @Test
    public void testDecode2() {
        assertThrows(NullPointerException.class, () -> this.codec.decode(null, 2, 1, StandardCharsets.UTF_8));

        assertThrows(DecodingException.class, () -> this.codec.decode("ABCabc".getBytes(), -1, 10, StandardCharsets.UTF_8));
        assertThrows(DecodingException.class, () -> this.codec.decode("ABCabc".getBytes(), 5, -2, StandardCharsets.UTF_8));
        assertThrows(DecodingException.class, () -> this.codec.decode("ABCabc".getBytes(), 0, 10, StandardCharsets.UTF_8));
        assertThrows(DecodingException.class, () -> this.codec.decode("ABCabc".getBytes(), 10, -1, StandardCharsets.UTF_8));
    }

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, int length, Charset set, StringBuilder value, byte[] actual) {
        this.codec.encode(datagram, byteOffset, length, Charset.defaultCharset(), value);
        assertArrayEquals(datagram, actual);
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.encode(null, 0, 8, StandardCharsets.UTF_8, new StringBuilder("ABC")));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, -1, 2, StandardCharsets.UTF_8, new StringBuilder("ABC")));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 10, -1, StandardCharsets.UTF_8, new StringBuilder("ABC")));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(datagram, 2, 10, StandardCharsets.UTF_8, new StringBuilder("ABC")));
    }
}
