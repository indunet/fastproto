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
import org.indunet.fastproto.annotation.BoolType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

/**
 * Codec for Boolean type.
 * This codec is responsible for encoding and decoding Boolean types.
 * It is used in conjunction with the BoolType annotation.
 * The codec ensures that the Boolean value is correctly encoded and decoded.
 * If there are any issues during the encoding or decoding process, an exception is thrown.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class BoolCodec implements Codec<Boolean> {
    @Override
    public Boolean decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(BoolType.class);
            val order = context.getBitOrder(type::bitOrder);

            return inputStream.readBool(type.byteOffset(), type.bitOffset(), order);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding bool type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, Boolean value) {
        try {
            val type = context.getDataTypeAnnotation(BoolType.class);
            val bitOrder = context.getBitOrder(type::bitOrder);

            outputStream.writeBool(type.byteOffset(), type.bitOffset(), bitOrder, value);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding the bool type.", e);
        }
    }
}
