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
import org.indunet.fastproto.annotation.type.Int8ArrayType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.util.stream.IntStream;

/**
 * Int8 array type codec.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class Int8ArrayCodec implements Codec<int[]> {
    public int[] decode(byte[] bytes, int offset, int length) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            val l = CodecUtils.reverse(bytes, offset, length);

            return IntStream.range(0, l)
                    .map(i -> CodecUtils.int8Type(bytes, o + i))
                    .toArray();
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding int8 array type.", e);
        }
    }

    public void encode(byte[] bytes, int offset, int length, int[] values) {
        try {
            val o = CodecUtils.reverse(bytes, offset);
            val l = CodecUtils.reverse(bytes, offset, length);

            IntStream.range(0, l)
                    .forEach(i -> CodecUtils.int8Type(bytes, o + i, values[i]));
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding int8 array type.", e);
        }
    }

    @Override
    public int[] decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(Int8ArrayType.class);

        return this.decode(bytes, type.offset(), type.length());
    }

    @Override
    public void encode(CodecContext context, byte[] bytes, int[] value) {
        val type = context.getDataTypeAnnotation(Int8ArrayType.class);

        this.encode(bytes, type.offset(), type.length(), value);
    }
}
