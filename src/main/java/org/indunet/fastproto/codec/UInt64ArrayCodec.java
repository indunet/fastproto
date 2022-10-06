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
import lombok.var;
import org.indunet.fastproto.annotation.type.UInt64ArrayType;
import org.indunet.fastproto.annotation.type.UInt64Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * UInt64 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class UInt64ArrayCodec implements Codec<BigInteger[]> {
    public BigInteger[] decode(byte[] bytes, int offset, int length) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * UInt64Type.SIZE)  / UInt64Type.SIZE + 1;
            }

            return IntStream.range(0, l)
                    .mapToObj(i -> CodecUtils.uint64Type(bytes, o + i * UInt64Type.SIZE))
                    .toArray(BigInteger[]::new);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding uint64 array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, BigInteger[] values) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            var l = length;

            if (l < 0) {
                l = CodecUtils.reverse(bytes, offset, length * UInt64Type.SIZE)  / UInt64Type.SIZE + 1;
            }

            IntStream.range(0, l)
                    .forEach(i -> CodecUtils.uint64Type(bytes, o + i * UInt64Type.SIZE, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException e) {
            throw new EncodingException("Fail encoding uint64 array type.", e);
        }
    }

    @Override
    public BigInteger[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(UInt64ArrayType.class);

        return this.decode(bytes, type.offset(), type.length());
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, BigInteger[] value) {
        val type = context.getDataTypeAnnotation(UInt64ArrayType.class);

        this.encode(bytes, type.offset(), type.length(), value);
    }
}