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
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.UInt64Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.math.BigInteger;

/**
 * UInt64 type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class UInt64Codec implements Codec<BigInteger> {
    public BigInteger decode(byte[] bytes, int offset, ByteOrder policy) {
        try {
            return CodecUtils.uint64Type(bytes, offset, policy);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Fail decoding uint64 type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, ByteOrder policy, BigInteger value) {
        try {
            CodecUtils.uint64Type(bytes, offset, policy, value);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding uint64 type.", e);
        }
    }

    @Override
    public BigInteger decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(UInt64Type.class);
        val order = context.getByteOrder(type::byteOrder);

        return this.decode(bytes, type.offset(), order);
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, BigInteger value) {
        val type = context.getDataTypeAnnotation(UInt64Type.class);
        val order = context.getByteOrder(type::byteOrder);

        this.encode(bytes, type.offset(), order, value);
    }
}
