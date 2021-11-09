/*
 * Copyright 2019-2021 indunet.org
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

package org.indunet.fastproto.decoder;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.ArrayType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.OutOfBoundsException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.ReverseUtils;
import org.indunet.fastproto.util.TypeUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * List type.
 *
 * @author Deng Ran
 * @see org.indunet.fastproto.decoder.TypeDecoder
 * @since 2.3.0
 */
public class ListDecoder implements TypeDecoder<List<?>> {
    @Override
    public List<?> decode(DecodeContext context) {
        val type = context.getTypeAnnotation(ArrayType.class);

        return decode(context.getDatagram(), type.value(), type.length(),
                type.protocolType(), context.getEndianPolicy());
    }

    public List decode(@NonNull final byte[] datagram, int byteOffset, int length,
                       @NonNull ProtocolType type, @NonNull EndianPolicy policy) {
        int size = TypeUtils.size(type);
        int bo = ReverseUtils.offset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new DecodingException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo >= datagram.length) {
            throw new DecodingException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (length <= 0) {
            throw new DecodingException(CodecError.ILLEGAL_PARAMETER);
        } else if (bo + size * length > datagram.length) {
            throw new OutOfBoundsException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        BiFunction<Function<Integer, ?>, List, List> codec = (func, list) -> {
            IntStream.range(0, length)
                    .parallel()
                    .forEachOrdered(i -> {
                        list.add(func.apply(i * size + bo));
                    });

            return list;
        };

        switch (type) {
            case CHARACTER:
                return codec.apply(b -> CodecUtils.characterType(datagram, b, policy), new ArrayList<Character>());
            case BYTE:
                return codec.apply(b -> CodecUtils.byteType(datagram, b), new ArrayList<Byte>());
            case SHORT:
                return codec.apply(b -> CodecUtils.shortType(datagram, b, policy), new ArrayList<Short>());
            case INTEGER:
                return codec.apply(b -> CodecUtils.integerType(datagram, b, policy), new ArrayList<Integer>());
            case LONG:
                return codec.apply(b -> CodecUtils.longType(datagram, b, policy), new ArrayList<Long>());
            case UINTEGER8:
                return codec.apply(b -> CodecUtils.uinteger8Type(datagram, b), new ArrayList<Integer>());
            case UINTEGER16:
                return codec.apply(b -> CodecUtils.uinteger16Type(datagram, b, policy), new ArrayList<Integer>());
            case UINTEGER32:
                return codec.apply(b -> CodecUtils.uinteger32Type(datagram, b, policy), new ArrayList<Long>());
            case INTEGER8:
                return codec.apply(b -> CodecUtils.integer8Type(datagram, b), new ArrayList<Integer>());
            case INTEGER16:
                return codec.apply(b -> CodecUtils.integer16Type(datagram, b, policy), new ArrayList<Integer>());
            case FLOAT:
                return codec.apply(b -> CodecUtils.floatType(datagram, b, policy), new ArrayList<Float>());
            case DOUBLE:
                return codec.apply(b -> CodecUtils.doubleType(datagram, b, policy), new ArrayList<Double>());
            default:
                throw new DecodingException(MessageFormat.format(
                        CodecError.NOT_SUPPORT_ARRAY_TYPE.getMessage(), type.toString()));
        }
    }
}
