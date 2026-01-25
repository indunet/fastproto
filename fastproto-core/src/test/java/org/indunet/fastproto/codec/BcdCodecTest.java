/*
 * Copyright 2019-2025 indunet.org
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
import org.indunet.fastproto.annotation.BcdType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test of BCD type codec.
 *
 * @author Deng Ran
 * @since 3.13.0
 */
public class BcdCodecTest {
    BcdCodec codec = new BcdCodec();

    @Test
    public void testDecodeInvalid() {
        byte[] bytes = new byte[4];

        // Null input stream
        assertThrows(NullPointerException.class,
                () -> this.codec.decode(mock(0, 2, ByteOrder.BIG), (ByteBufferInputStream) null));

        // Out of range offset
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(-1, 2, ByteOrder.BIG), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(4, 2, ByteOrder.BIG), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testEncodeInvalid() {
        byte[] bytes = new byte[4];

        // Null output stream
        assertThrows(NullPointerException.class,
                () -> this.codec.encode(mock(0, 2, ByteOrder.BIG), (ByteBufferOutputStream) null, 12));

        // Out of range offset
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(4, 2, ByteOrder.BIG), new ByteBufferOutputStream(bytes), 12));

        // Negative not supported
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(0, 2, ByteOrder.BIG), new ByteBufferOutputStream(bytes), -1));
    }

    @Test
    public void testEncodeDecodeBigEndian() {
        byte[] bytes = new byte[4];
        ByteBufferOutputStream out = new ByteBufferOutputStream(bytes);

        // 2 bytes BCD -> 4 digits
        this.codec.encode(mock(0, 2, ByteOrder.BIG), out, 1234);

        ByteBufferInputStream in = new ByteBufferInputStream(bytes);
        int value = this.codec.decode(mock(0, 2, ByteOrder.BIG), in);
        assertEquals(1234, value);
    }

    @Test
    public void testEncodeDecodeLittleEndian() {
        byte[] bytes = new byte[4];
        ByteBufferOutputStream out = new ByteBufferOutputStream(bytes);

        // 2 bytes BCD -> 4 digits, little-endian
        this.codec.encode(mock(0, 2, ByteOrder.LITTLE), out, 9876);

        ByteBufferInputStream in = new ByteBufferInputStream(bytes);
        int value = this.codec.decode(mock(0, 2, ByteOrder.LITTLE), in);
        assertEquals(9876, value);
    }

    protected CodecContext mock(int offset, int length, ByteOrder order) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(BcdType.class, offset, length, order))
                .build();
    }
}


