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

package org.indunet.fastproto;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.annotation.EnableCompress;
import org.indunet.fastproto.compress.CompressorFactory;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.DecoderFactory;
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.encoder.EncoderFactory;
import org.indunet.fastproto.exception.*;
import org.indunet.fastproto.flow.CodecContext;
import org.indunet.fastproto.flow.FlowFactory;
import org.indunet.fastproto.integrity.Checker;
import org.indunet.fastproto.integrity.CheckerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * FastProto API.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
public class FastProto {
    /**
     * Convert binary message into object.
     *
     * @param datagram binary message
     * @param clazz    deserialized object
     * @return deserialize object instance
     */
    public static <T> T parseFrom(@NonNull byte[] datagram, @NonNull Class<T> clazz) {
        return parseFrom(datagram, clazz, CodecFeature.DEFAULT);
    }

    /**
     * Convert binary datagram into object.
     *
     * @param datagram       binary message
     * @param protocolClass  deserialize object
     * @param enableCompress enable compress or not
     * @return object.
     */
    @Deprecated
    public static <T> T parseFrom(@NonNull byte[] datagram, @NonNull Class<T> protocolClass, boolean enableCompress) {
        if (enableCompress && protocolClass.isAnnotationPresent(EnableCompress.class)) {
            val compress = protocolClass.getAnnotation(EnableCompress.class);
            val compressor = CompressorFactory.create(compress);

            datagram = compressor.decompress(datagram);
        }

        // Check sum.
        if (protocolClass.isAnnotationPresent(Checksum.class)) {
            Checker checker = CheckerFactory.create(protocolClass.getAnnotation(Checksum.class));

            if (!checker.validate(datagram, protocolClass)) {
                throw new CheckSumException(CodecError.ILLEGAL_CHECK_SUM);
            }
        }

        // Protocol version.
        if (!VersionAssist.validate(datagram, protocolClass)) {
            throw new ProtocolVersionException(CodecError.PROTOCOL_VERSION_NOT_MATCH);
        }

        TypeAssist assist = TypeAssist.byClass(protocolClass);
        List<DecodeContext> contexts = assist.toDecodeContexts(datagram);

        contexts.parallelStream()
                .forEach(c -> {
                    TypeAssist a = c.getTypeAssist();
                    Function<DecodeContext, ?> func = DecoderFactory.getDecoder(
                            a.getDecoderClass(),
                            a.getDecodeFormula());
                    try {
                        Object value = func.apply(c);
                        Object o = c.getObject();
                        a.setValue(o, value);
                    } catch (DecodeException e) {
                        throw new DecodeException(MessageFormat.format(
                                CodecError.FAIL_DECODING_FIELD.getMessage(), a.getField().toString()), e);
                    }
                });

        return assist.getObject(protocolClass);
    }

    public static <T> T parseFrom(@NonNull byte[] datagram, @NonNull Class<T> protocolClass, int codecFeature) {
        TypeAssist assist = TypeAssist.byClass(protocolClass);
        CodecContext context = CodecContext.builder()
                .datagram(datagram)
                .protocolClass(protocolClass)
                .codecFeature(codecFeature)
                .typeAssist(assist)
                .build();
        FlowFactory.createDecode(assist.getCodecFeature() | codecFeature)
                .process(context);

        return (T) context.getObject();
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object serialized object
     * @return binary datagram.
     */
    public static byte[] toByteArray(@NonNull Object object) {
        return toByteArray(object, -1, CodecFeature.DEFAULT);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object         serialized object
     * @param enableCompress enable compress
     * @return binary datagram.
     */
    @Deprecated
    public static byte[] toByteArray(@NonNull Object object, boolean enableCompress) {
//        TypeAssist assist = assists.computeIfAbsent(object.getClass(), c -> TypeAssist.head(c));
//        int length = assist.getMaxLength();
//        length += CheckerUtils.getSize(object.getClass());
//        length += VersionAssist.getSize(object.getClass());
//
//        return toByteArray(object, length, enableCompress);

        if (enableCompress) {
            return toByteArray(object, -1, CodecFeature.IGNORE_ENABLE_COMPRESS);
        } else {
            return toByteArray(object, -1, CodecFeature.DEFAULT);
        }
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object serialized object
     * @param length the length of the datagram.
     * @return binary datagram.
     */
    public static byte[] toByteArray(@NonNull Object object, int length) {
        return toByteArray(object, length, CodecFeature.DEFAULT);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object         serialized object
     * @param length         the length of the datagram.
     * @param enableCompress enable compress or not
     * @return binary datagram.
     */
    @Deprecated
    public static byte[] toByteArray(@NonNull Object object, int length, boolean enableCompress) {
        byte[] datagram = new byte[length];

        TypeAssist assist = TypeAssist.byClass(object.getClass());
        List<EncodeContext> contexts = assist.toEncodeContexts(object, datagram);

        contexts.stream()
                .forEach(c -> {
                    TypeAssist a = c.getTypeAssist();

                    try {
                        if (a.getEncodeFormula() != null) {
                            Object o = EncoderFactory.getFormula(c.getTypeAssist().getEncodeFormula())
                                    .apply(c.getValue());
                            c.setValue(o);
                        }

                        Consumer<EncodeContext> consumer = EncoderFactory.getEncoder(c.getTypeAssist().getEncoderClass());
                        consumer.accept(c);
                    } catch (EncodeException e) {
                        throw new EncodeException(MessageFormat.format(
                                CodecError.FAIL_ENCODING_FIELD.getMessage(),
                                    a.getField().toString()), e);
                    }
                });

        // Protocol version.
        VersionAssist.encode(datagram, object.getClass());

        // Check sum.
        if (object.getClass().isAnnotationPresent(Checksum.class)) {
            Checksum checkSum = object.getClass().getAnnotation(Checksum.class);
            Checker checker = CheckerFactory.create(checkSum);

            checker.setValue(datagram, object.getClass());
        }

        if (enableCompress && object.getClass().isAnnotationPresent(EnableCompress.class)) {
            val annotation = object.getClass().getAnnotation(EnableCompress.class);
            val compressor = CompressorFactory.create(annotation);

            return compressor.compress(datagram);
        } else {
            return datagram;
        }
    }

    public static byte[] toByteArray(@NonNull Object object, int length, int codecFeature) {
        TypeAssist assist = TypeAssist.byClass(object.getClass());
        CodecContext.CodecContextBuilder builder = CodecContext.builder()
                .object(object)
                .protocolClass(object.getClass())
                .codecFeature(codecFeature)
                .typeAssist(assist);
        CodecContext context;

        if (length == -1) {
            context = builder.build();
            FlowFactory.createEncode(assist.getCodecFeature() | codecFeature)
                    .process(context);
        } else {
            context = builder
                    .datagram(new byte[length])
                    .build();
            FlowFactory.createEncode(codecFeature | CodecFeature.NON_INFER_LENGTH)
                    .process(context);
        }

        return context.getDatagram();
    }
}
