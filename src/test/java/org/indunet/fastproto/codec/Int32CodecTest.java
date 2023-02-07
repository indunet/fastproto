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
import org.indunet.fastproto.annotation.Int32Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test of int32 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class Int32CodecTest {
    Int32Codec codec = new Int32Codec();

    @Test
    public void testDecode() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.decode(mock(0, ByteOrder.LITTLE), (ByteBufferInputStream) null));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(-1, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class,
                () -> this.codec.decode(mock(10, ByteOrder.LITTLE), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testEncode() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.encode(mock(0, ByteOrder.BIG), (ByteBufferOutputStream) null, 8));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(-1, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), 11));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(8, ByteOrder.LITTLE), new ByteBufferOutputStream(bytes), 12));
    }

    protected CodecContext mock(int offset, ByteOrder order) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(Int32Type.class, offset, order))
                .build();
    }
}
