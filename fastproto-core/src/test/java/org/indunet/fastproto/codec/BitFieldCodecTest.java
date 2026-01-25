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

import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.BitFieldType;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test of bit field codec.
 */
public class BitFieldCodecTest {
    BitFieldCodec codec = new BitFieldCodec();

    @Test
    public void testDecodeLsbLittleCrossByte() {
        // byte0=0xC0, byte1=0x1A -> expected value 0b1101011 (107) with LSB_0 + LITTLE, offset 0/bitOffset 6, len 7
        byte[] bytes = new byte[] {(byte) 0xC0, 0x1A};
        CodecContext ctx = ctx(BitOrder.LSB_0, ByteOrder.LITTLE, 0, 6, 7);

        int value = codec.decode(ctx, new ByteBufferInputStream(bytes));
        assertEquals(107, value);
    }

    @Test
    public void testDecodeMsbBigSingleByte() {
        byte[] bytes = new byte[] {(byte) 0x58};
        CodecContext ctx = ctx(BitOrder.MSB_0, ByteOrder.BIG, 0, 1, 6);

        int value = codec.decode(ctx, new ByteBufferInputStream(bytes));
        assertEquals(44, value);   // bits 6..1 => 1 0 1 1 0 0 -> 0b101100
    }

    @Test
    public void testEncodeRangeCheck() {
        byte[] bytes = new byte[2];
        CodecContext ctx = ctx(BitOrder.LSB_0, ByteOrder.LITTLE, 0, 0, 5);

        assertThrows(EncodingException.class,
                () -> codec.encode(ctx, new ByteBufferOutputStream(bytes), 64)); // 5 bits max 31
    }

    @Test
    public void testEncodeDecodeRoundTrip() {
        byte[] bytes = new byte[3];
        CodecContext ctx = ctx(BitOrder.MSB_0, ByteOrder.BIG, 0, 3, 10);
        ByteBufferOutputStream out = new ByteBufferOutputStream(bytes);

        codec.encode(ctx, out, 0x155);   // 0b101010101

        int value = codec.decode(ctx, new ByteBufferInputStream(bytes));
        assertEquals(0x155, value);
    }

    protected CodecContext ctx(BitOrder bitOrder, ByteOrder byteOrder, int offset, int bitOffset, int length) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", offset);
        map.put("bitOffset", bitOffset);
        map.put("length", length);
        map.put("bitOrder", new BitOrder[] {bitOrder});
        map.put("byteOrder", new ByteOrder[] {byteOrder});

        return CodecContext.builder()
                .dataTypeAnnotation(AnnotationUtils.mock(BitFieldType.class, map))
                .build();
    }
}

