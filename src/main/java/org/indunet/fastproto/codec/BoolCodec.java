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
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.annotation.BoolType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.util.Arrays;

/**
 * Bool type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class BoolCodec implements Codec<Boolean> {
    public boolean decode(byte[] bytes, int byteOffset, int bitOffset) {
        try {
            return CodecUtils.boolType(bytes, byteOffset, bitOffset);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding boolean type.", e);
        }
    }

    public void encode(byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        try {
            CodecUtils.boolType(datagram, byteOffset, bitOffset, value);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding the boolean type.", e);
        }
    }
    
    @Override
    public Boolean decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(BoolType.class);
        val bitOrder = Arrays.stream(type.bitOrder())
                .findFirst()
                .orElseGet(context::getDefaultBitOrder);

        if (bitOrder == BitOrder.LSB_0) {
            return this.decode(bytes, type.byteOffset(), type.bitOffset());
        } else if (bitOrder == BitOrder.MSB_0) {
            return this.decode(bytes, type.byteOffset(), 7 - type.bitOffset());
        } else {
            throw new DecodingException("Illegal mode, only LSB_0 or MSB_0 can be used.");
        }
    }
    
    @Override
    public void encode(CodecContext context, byte[] bytes, Boolean value) {
        val type = context.getDataTypeAnnotation(BoolType.class);
        val bitOrder = Arrays.stream(type.bitOrder())
                .findFirst()
                .orElseGet(context::getDefaultBitOrder);

        if (bitOrder == BitOrder.LSB_0) {
            this.encode(bytes, type.byteOffset(), type.bitOffset(), value);
        } else if (bitOrder == BitOrder.MSB_0) {
            this.encode(bytes, type.byteOffset(), 7 - type.bitOffset(), value);
        } else {
            throw new EncodingException("Illegal mode, only LSB_0 or MSB_0 can be used.");
        }
    }
}
