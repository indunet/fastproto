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
import org.indunet.fastproto.annotation.Int16ArrayType;
import org.indunet.fastproto.annotation.Int32ArrayType;
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
 * Unit test of int32 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class Int32ArrayCodecTest {
    Int32ArrayCodec codec = new Int32ArrayCodec();

    @Test
    public void testDecode1() {
        val random = new Random();
        val expected = IntStream.range(0, 10)
                .map(i -> random.nextInt())
                .toArray();

        val bytes = BinaryUtils.int32Of(expected, ByteOrder.LITTLE);

        assertArrayEquals(codec.decode(mock(0, 2, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 0, 2));
        assertArrayEquals(codec.decode(mock(12, 4, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 3, 7));
        assertArrayEquals(codec.decode(mock(20, 5, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 5, 10));
        assertArrayEquals(codec.decode(mock(24, -1, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 6, 10));
        assertArrayEquals(codec.decode(mock(-16, -1, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 6, 10));
        assertArrayEquals(codec.decode(mock(-16, -2, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)), Arrays.copyOfRange(expected, 6, 9));
    }

    @Test
    public void testDecode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.decode(mock(2, 10, ByteOrder.LITTLE), (ByteBufferInputStream) null));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(2, 10, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(-2, 10, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(2, -10, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(10, -1, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testEncode1() {
        val random = new Random();
        val values = new int[16];

        IntStream.range(0, 16)
                .forEach(i -> values[i] = random.nextInt());

        val bytes = new byte[16 * 4];

        this.codec.encode(mock(0, 16, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), values);
        assertArrayEquals(bytes, BinaryUtils.int32Of(values, ByteOrder.LITTLE));

        this.codec.encode(mock(0, -1, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), values);
        assertArrayEquals(bytes, BinaryUtils.int32Of(values, ByteOrder.LITTLE));
    }

    @Test
    public void testEncode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.encode(mock(0, -1, ByteOrder.LITTLE), (ByteBufferOutputStream) null, new int[8]));
        assertThrows(NullPointerException.class,
                () -> this.codec.encode(mock(0, -1, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), null));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(-2, -7, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), new int[8]));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(-11, -7, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), new int[8]));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(10, -1, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), new int[8]));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(0, 11, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), new int[8]));
    }

    protected CodecContext mock(int offset, int length, ByteOrder order) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(Int32ArrayType.class, offset, length, order))
                .build();
    }
}
