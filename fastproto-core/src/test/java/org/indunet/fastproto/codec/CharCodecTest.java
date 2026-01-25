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

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.CharType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of CharCodec
 *
 * @author Deng Ran
 * @since 3.8.4
 */
public class CharCodecTest {
    CharCodec codec = new CharCodec();

    @Test
    public void testDecode1() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) ('中' & 0xFF);
        bytes[1] = (byte) ('中' >>> 8 & 0xFF);

        bytes[3] = (byte) ('文' & 0xFF);
        bytes[2] = (byte) ('文' >>> 8 & 0xFF);

        assertEquals('中', codec.decode(mock(0, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertEquals('文', codec.decode(mock(2, ByteOrder.BIG), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testDecode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(mock(10, ByteOrder.LITTLE), (ByteBufferInputStream) null));

        assertThrows(DecodingException.class, () -> this.codec.decode(mock(10, ByteOrder.BIG), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testEncode1() {
        byte[] actual = new byte[10];

        this.codec.encode(mock(0, ByteOrder.LITTLE), new ByteBufferOutputStream(actual), '中');
        this.codec.encode(mock(2 - actual.length, ByteOrder.BIG), new ByteBufferOutputStream(actual), '文');

        byte[] expected = new byte[10];

        expected[0] = (byte) ('中' & 0xFF);
        expected[1] = (byte) ('中' >>> 8 & 0xFF);

        expected[3] = (byte) ('文' & 0xFF);
        expected[2] = (byte) ('文' >>> 8 & 0xFF);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEncode2() {
        assertThrows(NullPointerException.class, () -> this.codec.encode(mock(0, ByteOrder.BIG), (ByteBufferOutputStream) null, 'A'));
    }

    protected CodecContext mock(int offset, ByteOrder order) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(CharType.class, offset, order))
                .build();
    }
}