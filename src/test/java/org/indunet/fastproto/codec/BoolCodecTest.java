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

import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.io.ByteBuffer;
import org.indunet.fastproto.annotation.BoolType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import static org.indunet.fastproto.annotation.BoolType.BIT_0;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of bool type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class BoolCodecTest {
    BoolCodec codec = new BoolCodec();

    @Test
    public void testDecode() {
        byte[] bytes = new byte[10];

        assertThrows(DecodingException.class, () -> this.codec.decode(mock(-101, BIT_0, BitOrder.LSB_0), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class, () -> this.codec.decode(mock(0, -1, BitOrder.LSB_0), new ByteBufferInputStream(bytes)));
        assertThrows(DecodingException.class, () -> this.codec.decode(mock(10, 0, BitOrder.LSB_0), new ByteBufferInputStream(bytes)));
    }

    @Test
    public void testEncode() {
        byte[] bytes = new byte[10];

        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(-101, 1, BitOrder.LSB_0), new ByteBufferOutputStream(bytes), true));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(10, 1, BitOrder.LSB_0), new ByteBufferOutputStream(bytes), true));
    }

    protected CodecContext mock(int byteOffset, int bitOffset, BitOrder order) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(BoolType.class, byteOffset, bitOffset, order))
                .build();
    }
}
