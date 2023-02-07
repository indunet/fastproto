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
import org.indunet.fastproto.annotation.DoubleArrayType;
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
 * Unit test of double array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class DoubleArrayCodecTest {
    DoubleArrayCodec codec = new DoubleArrayCodec();

    @Test
    public void testDecode1() {
        val expected = new double[] {0.19, 1.21, 2.31, 3.51, 0.46, 0.45, 6.91, 7.0, 8.11, 9.23};
        val bytes = BinaryUtils.valueOf(expected, ByteOrder.LITTLE);

        assertArrayEquals(Arrays.copyOfRange(expected, 0, 2), codec.decode(mock(0, 2, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertArrayEquals(Arrays.copyOfRange(expected, 3, 7), codec.decode(mock(24, 4, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertArrayEquals(Arrays.copyOfRange(expected, 5, 10), codec.decode(mock(40, 5, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertArrayEquals(Arrays.copyOfRange(expected, 6, 10), codec.decode(mock(48, -1, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertArrayEquals(Arrays.copyOfRange(expected, 6, 10), codec.decode(mock(-32, -1, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertArrayEquals(Arrays.copyOfRange(expected, 6, 9), codec.decode(mock(-32, -2, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testDecode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(mock(2, 10, ByteOrder.LITTLE), (ByteBufferInputStream) null));
        assertThrows(DecodingException.class, () -> this.codec.decode(mock(2, 10, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class, () -> this.codec.decode(mock(-2, 10, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class, () -> this.codec.decode(mock(2, -10, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class, () -> this.codec.decode(mock(10, -1, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testEncode1() {
        val random = new Random();
        val values = IntStream.range(0, 16)
                .mapToDouble(__ -> random.nextDouble())
                .toArray();
        val bytes = new byte[16 * 8];

        this.codec.encode(mock(0, 16, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), values);
        assertArrayEquals(bytes, BinaryUtils.valueOf(values, ByteOrder.LITTLE));

        this.codec.encode(mock(0, -1, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), values);
        assertArrayEquals(bytes, BinaryUtils.valueOf(values, ByteOrder.LITTLE));
    }

    @Test
    public void testEncode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.encode(mock(0, -1, ByteOrder.LITTLE), (ByteBufferOutputStream) null, new double[8]));
        assertThrows(NullPointerException.class, () -> this.codec.encode(mock(0, -1, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), null));

        assertThrows(EncodingException.class, () -> this.codec.encode(mock(-2, -7, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), new double[8]));
        assertThrows(EncodingException.class, () -> this.codec.encode(mock(-11, -7, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), new double[8]));
        assertThrows(EncodingException.class, () -> this.codec.encode(mock(10, -1, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), new double[8]));
        assertThrows(EncodingException.class, () -> this.codec.encode(mock(0, 11, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), new double[8]));
    }

    protected CodecContext mock(int offset, int length, ByteOrder order) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(DoubleArrayType.class, offset, length, order))
                .build();
    }
}
