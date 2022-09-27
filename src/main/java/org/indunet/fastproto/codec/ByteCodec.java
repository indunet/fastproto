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

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.annotation.type.Int8Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CodecUtils;

/**
 * Byte type codec.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class ByteCodec implements Codec<Byte> {
    public byte decode(@NonNull final byte[] datagram, int offset) {
        try {
            return CodecUtils.byteType(datagram, offset);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding int8(byte) type.", e);
        }
    }

    public void encode(@NonNull byte[] datagram, int offset, byte value) {
        try {
            CodecUtils.byteType(datagram, offset, value);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding int8(byte) type.", e);
        }
    }
    
    @Override
    public Byte decode(CodecContext context, byte[] bytes) {
        val type = context.getDataTypeAnnotation(Int8Type.class);
    
        return this.decode(bytes, type.offset());
    }
    
    @Override
    public void encode(CodecContext context, byte[] bytes, Byte value) {
        val type = context.getDataTypeAnnotation(Int8Type.class);
    
        this.encode(bytes, type.offset(), value);
    }
}
