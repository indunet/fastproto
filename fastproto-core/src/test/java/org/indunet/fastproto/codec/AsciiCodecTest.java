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
import org.indunet.fastproto.annotation.AsciiType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of AsciiCodec
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class AsciiCodecTest {
    AsciiCodec codec = new AsciiCodec();

    @Test
    public void testDecode1() {
        byte[] bytes = new byte[10];

        bytes[0] = 'a';
        bytes[2] = 'A';

        assertEquals('a', (char) codec.decode(mock(0), new ByteBufferInputStream(bytes)));
        assertEquals('A', (char) codec.decode(mock(2), new ByteBufferInputStream(bytes)));
        assertEquals('A', (char) codec.decode(mock(2 - bytes.length), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testDecode2() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(mock(10), (ByteBufferInputStream) null));

        assertThrows(DecodingException.class, () -> this.codec.decode(mock(10), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testEncode1() {
        byte[] bytes = new byte[10];

        this.codec.encode(mock(0), new ByteBufferOutputStream(bytes), 'A');
        this.codec.encode(mock(2 - bytes.length), new ByteBufferOutputStream(bytes), 'a');

        byte[] cache = new byte[10];

        cache[0] = 65;
        cache[2] = 97;

        assertArrayEquals(cache, bytes);
    }

    @Test
    public void testEncode2() {
        assertThrows(NullPointerException.class, () -> this.codec.encode(mock(0), (ByteBufferOutputStream) null, 'A'));
    }

    protected CodecContext mock(int offset) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(AsciiType.class, offset, ByteOrder.LITTLE))
                .build();
    }
}