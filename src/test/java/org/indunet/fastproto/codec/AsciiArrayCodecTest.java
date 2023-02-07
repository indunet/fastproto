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
import org.indunet.fastproto.annotation.AsciiArrayType;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Unit test of ASCII array type codec.
 *
 * @author Deng Ran
 * @since 3.9.1
 */
public class AsciiArrayCodecTest {
    AsciiArrayCodec codec = new AsciiArrayCodec();

    @Test
    public void testDecode() {
        val expected = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        val bytes = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'};

        assertArrayEquals(expected, codec.decode(mock(0, 7), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testEncode() {
        val chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        val expected = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        val actual = new byte[expected.length];

        this.codec.encode(mock(0, 7), new ByteBufferOutputStream(actual), chars);
        assertArrayEquals(expected, actual);
    }

    protected CodecContext mock(int offset, int length) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(AsciiArrayType.class, offset, length))
                .build();
    }
}
