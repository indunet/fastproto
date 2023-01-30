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

import org.indunet.fastproto.ByteBuffer;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Int8Type;
import org.indunet.fastproto.annotation.UInt8Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of uint8 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class UInt8CodecTest {
    UInt8Codec codec = new UInt8Codec();

    @Test
    public void testDecode() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(mock(0), null));

        assertThrows(DecodingException.class, () -> this.codec.decode(mock(-101), bytes));
        assertThrows(DecodingException.class, () -> this.codec.decode(mock(10), bytes));
    }

    @Test
    public void testEncode() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.encode(mock(0), (ByteBuffer) null, 8));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(-101), new ByteBuffer(bytes), 1));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(10), new ByteBuffer(bytes), 1));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(0), new ByteBuffer(bytes), 256));
    }

    protected CodecContext mock(int offset) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(UInt8Type.class, offset, ByteOrder.LITTLE))
                .build();
    }
}
