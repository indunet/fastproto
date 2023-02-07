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

import lombok.val;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.UInt8ArrayType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.AnnotationUtils;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test of uint8 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class UInt8ArrayCodecTest {
    UInt8ArrayCodec codec = new UInt8ArrayCodec();

    @Test
    public void testDecode1() {
        val random = new Random();
        val expected = IntStream.range(0, 10)
                .map(i -> random.nextInt(256))
                .toArray();

        val bytes = BinaryUtils.uint8Of(expected);

        assertArrayEquals(codec.decode(mock(0, 2), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 0, 2));
        assertArrayEquals(codec.decode(mock(3, 4), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 3, 7));
        assertArrayEquals(codec.decode(mock(5, 5), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 5, 10));
        assertArrayEquals(codec.decode(mock(6, -1), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 6, 10));
        assertArrayEquals(codec.decode(mock(-4, -1), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 6, 10));
        assertArrayEquals(codec.decode(mock(-4, -2), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 6, 9));
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

    @Test
    public void testEncode1() {
        val random = new Random();
        val values = IntStream.range(0, 16)
                .map(__ -> random.nextInt(256))
                .toArray();
        val bytes = new byte[16];

        this.codec.encode(mock(0, 16), new ByteBufferOutputStream(bytes), values);
        assertArrayEquals(bytes, BinaryUtils.uint8Of(values));

        this.codec.encode(mock(0, -1), new ByteBufferOutputStream(bytes), values);
        assertArrayEquals(bytes, BinaryUtils.uint8Of(values));
    }

    @Test
    public void testEncode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.encode(mock(0, -1), (ByteBufferOutputStream) null, new int[8]));
        assertThrows(NullPointerException.class,
                () -> this.codec.encode(mock(0, -1), new ByteBufferOutputStream(bytes), null));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(-2, -7), new ByteBufferOutputStream(bytes), new int[8]));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(-11, -7), new ByteBufferOutputStream(bytes), new int[8]));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(10, -1), new ByteBufferOutputStream(bytes), new int[8]));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(0, 11), new ByteBufferOutputStream(bytes), new int[8]));
    }

    protected CodecContext mock(int offset, int length) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(UInt8ArrayType.class, offset, length, ByteOrder.LITTLE))
                .build();
    }
}
