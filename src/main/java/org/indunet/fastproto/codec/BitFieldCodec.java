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

import lombok.val;
import org.indunet.fastproto.annotation.BitFieldType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

/**
 * Codec for BitFieldType, unsigned width 1..31 bits mapped to Integer.
 */
public class BitFieldCodec implements Codec<Integer> {
    @Override
    public Integer decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(BitFieldType.class);
            val bitOrder = context.getBitOrder(type::bitOrder);
            val byteOrder = context.getByteOrder(type::byteOrder);
            int length = type.length();

            return inputStream.readBits(type.offset(), type.bitOffset(), length, bitOrder, byteOrder);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding bit field type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, Integer value) {
        try {
            val type = context.getDataTypeAnnotation(BitFieldType.class);
            val bitOrder = context.getBitOrder(type::bitOrder);
            val byteOrder = context.getByteOrder(type::byteOrder);
            int length = type.length();

            outputStream.writeBits(type.offset(), type.bitOffset(), length, bitOrder, byteOrder, value);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding bit field type.", e);
        }
    }
}

