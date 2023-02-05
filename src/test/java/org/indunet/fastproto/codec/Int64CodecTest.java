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

import org.indunet.fastproto.io.ByteBuffer;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Int64Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of int64 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class Int64CodecTest {
    Int64Codec codec = new Int64Codec();

    @Test
    public void testDecode() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class, () -> this.codec.decode(mock(0, ByteOrder.LITTLE), (byte[]) null));

        assertThrows(DecodingException.class, () -> this.codec.decode(mock(-1, ByteOrder.LITTLE), bytes));
        assertThrows(DecodingException.class, () -> this.codec.decode(mock(10, ByteOrder.LITTLE), bytes));
    }

    @Test
    public void testEncode() {
        byte[] bytes = new byte[10];

        assertThrows(NullPointerException.class,
                () -> this.codec.encode(mock(0, ByteOrder.BIG), (ByteBuffer) null, 8L));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(-1, ByteOrder.LITTLE), new ByteBuffer(bytes), -1L));
        assertThrows(EncodingException.class,
                () -> this.codec.encode(mock(8, ByteOrder.LITTLE), new ByteBuffer(bytes), -1L));
    }

    protected CodecContext mock(int offset, ByteOrder order) {
        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(Int64Type.class, offset, order))
                .build();
    }
}
