/*
 * Copyright 2019-2021 indunet
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
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.OutOfBoundsException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.ReverseUtils;
import org.indunet.fastproto.util.TypeUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Array decoder.
 *
 * @author Deng Ran
 * @since 2.2.0
 */
public class ArrayDecoder implements TypeDecoder<Object> {
    @Override
    public Object decode(DecodeContext context) {
        val type = context.getTypeAnnotation(ArrayType.class);
        val length = type.length();
        val componentType = context.getTypeAssist()
                .getField()
                .getType()
                .getComponentType();

        return decode(context.getDatagram(), type.value(), length,
                type.protocolType(), context.getEndianPolicy(), componentType.isPrimitive());
    }

    public Object decode(@NonNull final byte[] datagram, int byteOffset, int length,
                         @NonNull ProtocolType type, @NonNull EndianPolicy policy) {
        return this.decode(datagram, byteOffset, length, type, policy, false);
    }


    public Object decode(@NonNull final byte[] datagram, int byteOffset, int length,
                         @NonNull ProtocolType type, @NonNull EndianPolicy policy, boolean primitive) {
        int size = TypeUtils.size(type);
        int bo = ReverseUtils.offset(datagram.length, byteOffset);

        if (bo < 0) {
            throw new DecodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo >= datagram.length) {
            throw new DecodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (length <= 0) {
            throw new DecodeException(CodecError.ILLEGAL_PARAMETER);
        } else if (bo + size * length > datagram.length) {
            throw new OutOfBoundsException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        val list = new ArrayList<Object>();

        Consumer<Function<Integer, ?>> codec = (func) -> {
            IntStream.range(0, length)
                    .parallel()
                    .forEachOrdered(i -> {
                        list.add(func.apply(i * size + bo));
                    });
        };

        switch (type) {
            case CHARACTER:
                codec.accept(b -> CodecUtils.characterType(datagram, b, policy));
                return primitive ? TypeUtils.listToArray(list, new char[length]) : list.toArray(new Character[length]);
            case BYTE:
                codec.accept(b -> CodecUtils.byteType(datagram, b));
                return primitive ? TypeUtils.listToArray(list, new byte[length]) : list.toArray(new Byte[length]);
            case SHORT:
                codec.accept(b -> CodecUtils.shortType(datagram, b, policy));
                return primitive ? TypeUtils.listToArray(list, new short[length]) : list.toArray(new Short[length]);
            case INTEGER:
                codec.accept(b -> CodecUtils.integerType(datagram, b, policy));
                return primitive ? TypeUtils.listToArray(list, new int[length]) : list.toArray(new Integer[length]);
            case LONG:
                codec.accept(b -> CodecUtils.longType(datagram, b, policy));
                return primitive ? TypeUtils.listToArray(list, new long[length]) : list.toArray(new Long[length]);
            case UINTEGER8:
                codec.accept(b -> CodecUtils.uinteger8Type(datagram, b));
                return primitive ? TypeUtils.listToArray(list, new int[length]) : list.toArray(new Integer[length]);
            case UINTEGER16:
                codec.accept(b -> CodecUtils.uinteger16Type(datagram, b, policy));
                return primitive ? TypeUtils.listToArray(list, new int[length]) : list.toArray(new Integer[length]);
            case UINTEGER32:
                codec.accept(b -> CodecUtils.uinteger32Type(datagram, b, policy));
                return primitive ? TypeUtils.listToArray(list, new long[length]) : list.toArray(new Long[length]);
            case INTEGER8:
                codec.accept(b -> CodecUtils.integer8Type(datagram, b));
                return primitive ? TypeUtils.listToArray(list, new int[length]) : list.toArray(new Integer[length]);
            case INTEGER16:
                codec.accept(b -> CodecUtils.integer16Type(datagram, b, policy));
                return primitive ? TypeUtils.listToArray(list, new int[length]) : list.toArray(new Integer[length]);
            case FLOAT:
                codec.accept(b -> CodecUtils.floatType(datagram, b, policy));
                return primitive ? TypeUtils.listToArray(list, new float[length]) : list.toArray(new Float[length]);
            case DOUBLE:
                codec.accept(b -> CodecUtils.doubleType(datagram, b, policy));
                return primitive ? TypeUtils.listToArray(list, new double[length]) : list.toArray(new Double[length]);
            default:
                throw new DecodeException(MessageFormat.format(
                        CodecError.NOT_SUPPORT_ARRAY_TYPE.getMessage(), type.toString()));
        }
    }
}
