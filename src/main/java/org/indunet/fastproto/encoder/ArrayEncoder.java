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
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.exception.SpaceNotEnoughException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.ReverseUtils;
import org.indunet.fastproto.util.TypeUtils;

import java.lang.annotation.Annotation;
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
                type.genericType(), context.getEndianPolicy(), context.getValue());
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, int length,
                       @NonNull Class<? extends Annotation> type, @NonNull EndianPolicy policy, Object values) {
        int size = TypeUtils.size(type);
        int bo = ReverseUtils.offset(datagram.length, byteOffset);
        boolean primitive = values.getClass()
                .getComponentType()
                .isPrimitive();

        if (bo < 0) {
            throw new EncodingException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (bo >= datagram.length) {
            throw new EncodingException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (length <= 0) {
            throw new EncodingException(CodecError.ILLEGAL_PARAMETER);
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

        if (type == ProtocolType.CHAR) {
            codec.accept((b, i) -> CodecUtils.type(datagram, b, policy,
                    primitive ? ((char[]) values)[i] : ((Character[]) values)[i]));
        } else if (type == ProtocolType.BYTE) {
            codec.accept((b, i) -> CodecUtils.type(datagram, b, policy,
                    primitive ? ((byte[]) values)[i] : ((Byte[]) values)[i]));
        } else if (type == ProtocolType.SHORT) {
            codec.accept((b, i) -> CodecUtils.type(datagram, b, policy,
                    primitive ? ((short[]) values)[i] : ((Short[]) values)[i]));
        } else if (type == ProtocolType.INT32) {
            codec.accept((b, i) -> CodecUtils.type(datagram, b, policy,
                    primitive ? ((int[]) values)[i] : ((Integer[]) values)[i]));
        } else if (type == ProtocolType.LONG) {
            codec.accept((b, i) -> CodecUtils.type(datagram, b, policy,
                    primitive ? ((long[]) values)[i] : ((Long[]) values)[i]));
        } else if (type == ProtocolType.UINT8) {
            codec.accept((b, i) -> CodecUtils.uint8Type(datagram, b,
                    primitive ? ((int[]) values)[i] : ((Integer[]) values)[i]));
        } else if (type == ProtocolType.UINT16) {
            codec.accept((b, i) -> CodecUtils.uint16Type(datagram, b, policy,
                    primitive ? ((int[]) values)[i] : ((Integer[]) values)[i]));
        } else if (type == ProtocolType.UINT32) {
            codec.accept((b, i) -> CodecUtils.uint32Type(datagram, b, policy,
                    primitive ? ((long[]) values)[i] : ((Long[]) values)[i]));
        } else if (type == ProtocolType.INT8) {
            codec.accept((b, i) -> CodecUtils.int8Type(datagram, b,
                    primitive ? ((int[]) values)[i] : ((Integer[]) values)[i]));
        } else if (type == ProtocolType.INT16) {
            codec.accept((b, i) -> CodecUtils.int16Type(datagram, b, policy,
                    primitive ? ((int[]) values)[i] : ((Integer[]) values)[i]));
        } else if (type == ProtocolType.FLOAT) {
            codec.accept((b, i) -> CodecUtils.type(datagram, b, policy,
                    primitive ? ((float[]) values)[i] : ((Float[]) values)[i]));
        } else if (type == ProtocolType.DOUBLE) {
            codec.accept((b, i) -> CodecUtils.type(datagram, b, policy,
                    primitive ? ((double[]) values)[i] : ((Double[]) values)[i]));
        } else {
            throw new EncodingException(MessageFormat.format(
                    CodecError.NOT_SUPPORT_ARRAY_TYPE.getMessage(), type.toString()));
        }
    }
}
