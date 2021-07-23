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

package org.indunet.fastproto.encoder;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.ArrayType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;
import org.indunet.fastproto.util.EncodeUtils;
import org.indunet.fastproto.util.ReverseUtils;
import org.indunet.fastproto.util.TypeUtils;

import java.text.MessageFormat;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Array encoder.
 *
 * @author Deng Ran
 * @since 2.2.0
 */
public class ArrayEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        val type = context.getTypeAnnotation(ArrayType.class);

        this.encode(context.getDatagram(), type.value(), type.length(),
                type.protocolType(), context.getEndianPolicy(), context.getValue());
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, int length,
                       @NonNull ProtocolType type, @NonNull EndianPolicy policy, Object values) {
        int size = TypeUtils.size(type);
        int bo = ReverseUtils.byteOffset(datagram.length, byteOffset);
        boolean primitive = values.getClass()
                .getComponentType()
                .isPrimitive();

        if (bo < 0) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo >= datagram.length) {
            throw new EncodeException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (length <= 0) {
            throw new EncodeException(CodecError.ILLEGAL_PARAMETER);
        } else if (bo + size * length > datagram.length) {
            throw new SpaceNotEnoughException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        Consumer<BiConsumer<Integer, Integer>> codec = (consumer) -> {
            IntStream.range(0, length)
                    .parallel()
                    .forEach(i -> {
                        consumer.accept(i * size + byteOffset, i);
                    });
        };

        switch (type) {
            case CHARACTER:
                codec.accept((b, i) -> EncodeUtils.type(datagram, b, policy,
                        primitive ? ((char[]) values)[i] : ((Character[]) values)[i]));
                break;
            case BYTE:
                codec.accept((b, i) -> EncodeUtils.type(datagram, b, policy,
                        primitive ? ((byte[]) values)[i] : ((Byte[]) values)[i]));
                break;
            case SHORT:
                codec.accept((b, i) -> EncodeUtils.type(datagram, b, policy,
                        primitive ? ((short[]) values)[i] : ((Short[]) values)[i]));
                break;
            case INTEGER:
                codec.accept((b, i) -> EncodeUtils.type(datagram, b, policy,
                        primitive ? ((int[]) values)[i] : ((Integer[]) values)[i]));
                break;
            case LONG:
                codec.accept((b, i) -> EncodeUtils.type(datagram, b, policy,
                        primitive ? ((long[]) values)[i] : ((Long[]) values)[i]));
                break;
            case UINTEGER8:
                codec.accept((b, i) -> EncodeUtils.uInteger8Type(datagram, b,
                        primitive ? ((int[]) values)[i] : ((Integer[]) values)[i]));
                break;
            case UINTEGER16:
                codec.accept((b, i) -> EncodeUtils.uInteger16Type(datagram, b, policy,
                        primitive ? ((int[]) values)[i] : ((Integer[]) values)[i]));
                break;
            case UINTEGER32:
                codec.accept((b, i) -> EncodeUtils.uInteger32Type(datagram, b, policy,
                        primitive ? ((long[]) values)[i] : ((Long[]) values)[i]));
                break;
            case INTEGER8:
                codec.accept((b, i) -> EncodeUtils.integer8Type(datagram, b,
                        primitive ? ((int[]) values)[i] : ((Integer[]) values)[i]));
                break;
            case INTEGER16:
                codec.accept((b, i) -> EncodeUtils.integer16Type(datagram, b, policy,
                        primitive ? ((int[]) values)[i] : ((Integer[]) values)[i]));
                break;
            case FLOAT:
                codec.accept((b, i) -> EncodeUtils.type(datagram, b, policy,
                        primitive ? ((float[]) values)[i] : ((Float[]) values)[i]));
                break;
            case DOUBLE:
                codec.accept((b, i) -> EncodeUtils.type(datagram, b, policy,
                        primitive ? ((double[]) values)[i] : ((Double[]) values)[i]));
                break;
            default:
                throw new EncodeException(MessageFormat.format(
                        CodecError.NOT_SUPPORT_ARRAY_TYPE.getMessage(), type.toString()));
        }
    }
}
