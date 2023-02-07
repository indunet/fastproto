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

import org.indunet.fastproto.annotation.StringType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.AnnotationUtils;
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
 * Unit test of string type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class StringCodecTest {
    StringCodec codec = new StringCodec();

    public static List<Arguments> testDecode1() {
        return Stream.of(
                Arguments.arguments("ABCabc".getBytes(), 0, -1, StandardCharsets.UTF_8, "ABCabc"),
                Arguments.arguments("abcdef".getBytes(), 3, 3, StandardCharsets.UTF_8, "def"),
                Arguments.arguments("abcdef".getBytes(), -3, 3, StandardCharsets.UTF_8, "def")
        ).collect(Collectors.toList());
    }

    public static List<Arguments> testEncode1() {
        return Stream.of(
                Arguments.arguments(new byte[6], 0, -1, StandardCharsets.UTF_8, "ABCabc", "ABCabc".getBytes()),
                Arguments.arguments(new byte[6], -6, -1, StandardCharsets.UTF_8, "ABCabc", "ABCabc".getBytes()),
                Arguments.arguments(new byte[8], 2, 6, StandardCharsets.UTF_8, "ABCabc", new byte[]{0, 0, 'A', 'B', 'C', 'a', 'b', 'c'})
        ).collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] actuals, int offset, int length, Charset set, String expecteds) {
        assertEquals(expecteds, codec.decode(mock(offset, length, set.name()), new ByteBufferInputStream(actuals)));
    }

    @Test
    public void testDecode2() {
        assertThrows(NullPointerException.class,
                () -> this.codec.decode(mock(2, 1, StandardCharsets.UTF_8.name()), (ByteBufferInputStream) null));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(-1, 10, StandardCharsets.UTF_8.name()), new ByteBufferInputStream("ABCabc".getBytes())));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(5, -2, StandardCharsets.UTF_8.name()), new ByteBufferInputStream("ABCabc".getBytes())));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(0, 10, StandardCharsets.UTF_8.name()), new ByteBufferInputStream("ABCabc".getBytes())));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(10, -1, StandardCharsets.UTF_8.name()), new ByteBufferInputStream("ABCabc".getBytes())));
    }

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] actuals, int offset, int length, Charset set, String value, byte[] expecteds) {
        this.codec.encode(mock(offset, length, set.name()), new ByteBufferOutputStream(actuals), value);
        assertArrayEquals(expecteds, actuals);
    }

    @Test
    public void testEncode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.encode(mock(0, 8, StandardCharsets.UTF_8.name()), (ByteBufferOutputStream) null, "ABC"));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(-1, 2, StandardCharsets.UTF_8.name()), new ByteBufferOutputStream(bytes), "ABC"));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(10, -1, StandardCharsets.UTF_8.name()), new ByteBufferOutputStream(bytes), "ABC"));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(8, 4, StandardCharsets.UTF_8.name()), new ByteBufferOutputStream(bytes), "ABCD"));
    }

    protected CodecContext mock(int offset, int length, String charset) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(StringType.class, offset, length, charset))
                .build();
    }
}
