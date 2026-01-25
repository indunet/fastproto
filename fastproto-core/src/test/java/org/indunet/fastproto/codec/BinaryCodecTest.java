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

import org.indunet.fastproto.annotation.BinaryType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.AnnotationUtils;
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
 * Unit test of binary codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class BinaryCodecTest {
    BinaryCodec codec = new BinaryCodec();

    @Test
    public void testDecode1() {
        byte[] bytes = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertArrayEquals(codec.decode(mock(0, 2), new ByteBufferInputStream(bytes)), new byte[] {0, 1});
        assertArrayEquals(codec.decode(mock(3, 4), new ByteBufferInputStream(bytes)), new byte[]{3, 4, 5, 6});
        assertArrayEquals(codec.decode(mock(5, 5), new ByteBufferInputStream(bytes)), new byte[]{5, 6, 7, 8, 9});
        assertArrayEquals(codec.decode(mock(6, -1), new ByteBufferInputStream(bytes)), new byte[]{6, 7, 8, 9});
        assertArrayEquals(codec.decode(mock(-4, -1), new ByteBufferInputStream(bytes)), new byte[]{6, 7, 8, 9});
        assertArrayEquals(codec.decode(mock(-4, -2), new ByteBufferInputStream(bytes)), new byte[]{6, 7, 8});
    }

    @Test
    public void testDecode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(mock(2, 10), (ByteBufferInputStream) null));

        assertThrows(DecodingException.class, () -> this.codec.decode(mock(2, 10), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class, () -> this.codec.decode(mock(-2, 10), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class, () -> this.codec.decode(mock(2, -10), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class, () -> this.codec.decode(mock(10, -1), new ByteBufferInputStream(bytes)));
    }

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] bytes, int byteOffset, int length, byte[] expecteds, byte[] actuals) {
        this.codec.encode(mock(byteOffset, length), new ByteBufferOutputStream(bytes), expecteds);
        assertArrayEquals(bytes, actuals);
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
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.encode(mock(0, -1), (ByteBufferOutputStream) null, new byte[8]));
        assertThrows(NullPointerException.class, () -> this.codec.encode(mock(0, 2), new ByteBufferOutputStream(), null));

        assertThrows(EncodingException.class, () -> this.codec.encode(mock(-2, -7), new ByteBufferOutputStream(bytes), new byte[8]));
        assertThrows(EncodingException.class, () -> this.codec.encode(mock(-11, -7), new ByteBufferOutputStream(bytes), new byte[8]));
        assertThrows(EncodingException.class, () -> this.codec.encode(mock(10, -1), new ByteBufferOutputStream(bytes), new byte[8]));
    }

    protected CodecContext mock(int offset, int length) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(BinaryType.class, offset, length))
                .build();
    }
}
