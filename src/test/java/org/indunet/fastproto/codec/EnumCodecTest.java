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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import org.indunet.fastproto.annotation.EnumType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of enum type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class EnumCodecTest {
    EnumCodec codec = new EnumCodec();

    @Test
    public void testDecode1() {
        val bytes = new byte[10];

        bytes[0] = 0;
        bytes[1] = 8;
        bytes[2] = 9;

        assertEquals(Color.GREEN, codec.decode(mock(0, "", Color.class), new ByteBufferInputStream(bytes)));
        assertEquals(Color.RED, codec.decode(mock(1, "code", Color.class), new ByteBufferInputStream(bytes)));
        assertEquals(Color.YELLOW, codec.decode(mock(2, "code", Color.class), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testDecode2() {
        val bytes = new byte[10];

        bytes[0] = 100;

        assertThrows(DecodingException.class, () -> codec.decode(mock(0, "number", Color.class), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class, () -> codec.decode(mock(0, "", Color.class), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class, () -> codec.decode(mock(0, "", Color.class), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testEncode1() {
        val bytes = new byte[10];
        val expected = new byte[10];

        expected[0] = 0;
        expected[1] = 8;
        expected[2] = 9;

        this.codec.encode(mock(0, "", Color.class), new ByteBufferOutputStream(bytes), Color.GREEN);
        this.codec.encode(mock(1, "code", Color.class), new ByteBufferOutputStream(bytes), Color.RED);
        this.codec.encode(mock(2, "code", Color.class), new ByteBufferOutputStream(bytes), Color.YELLOW);

        assertArrayEquals(expected, bytes);
    }

    @Test
    public void testEncode2() {
        val bytes = new byte[10];

        bytes[0] = 100;
        assertThrows(EncodingException.class,
                () -> codec.encode(mock(0, "number", Color.class), new ByteBufferOutputStream(bytes), Color.RED));
    }

    @AllArgsConstructor
    public enum Color {
        GREEN(0x01),
        RED(0x08),
        YELLOW(0x09);

        int code;
    }

    @Data
    public static class EnumObject {
        @EnumType(offset = 0)
        Color color1;

        @EnumType(offset = 1, name = "code")
        Color color2;
    }

    protected CodecContext mock(int offset, String name, Class<? extends Enum> enumClass) {
        return CodecContext.builder()
                .fieldType(enumClass)
                .dataTypeAnnotation(AnnotationUtils.mock(EnumType.class, offset, name))
                .build();
    }
}
