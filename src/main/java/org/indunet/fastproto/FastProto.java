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
import org.indunet.fastproto.annotation.CheckSum;
import org.indunet.fastproto.annotation.EnableCompress;
import org.indunet.fastproto.check.Checker;
import org.indunet.fastproto.check.CheckerFactory;
import org.indunet.fastproto.check.CheckerUtils;
import org.indunet.fastproto.compress.CompressorFactory;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.DecoderFactory;
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.encoder.EncoderFactory;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * FastProto API.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
public class FastProto {
    protected static ConcurrentHashMap<Class<?>, TypeAssist> assists = new ConcurrentHashMap<>();

    /**
     * Convert binary message into object.
     *
     * @param datagram binary message
     * @param clazz    deserialized object
     * @return deserialize object instance
     */
    public static <T> T parseFrom(@NonNull byte[] datagram, @NonNull Class<T> clazz) {
        return parseFrom(datagram, clazz, true);
    }

    /**
     * Convert binary datagram into object.
     *
     * @param datagram       binary message
     * @param protocolClass  deserialize object
     * @param enableCompress enable compress or not
     * @return object.
     */
    public static <T> T parseFrom(@NonNull byte[] datagram, @NonNull Class<T> protocolClass, boolean enableCompress) {
        if (enableCompress && protocolClass.isAnnotationPresent(EnableCompress.class)) {
            val compress = protocolClass.getAnnotation(EnableCompress.class);
            val compressor = CompressorFactory.create(compress);

            datagram = compressor.decompress(datagram);
        }

        // Check sum.
        if (protocolClass.isAnnotationPresent(CheckSum.class)) {
            Checker checker = CheckerFactory.create(protocolClass.getAnnotation(CheckSum.class));

            if (!checker.validate(datagram, protocolClass)) {
                throw new DecodeException(DecodeError.ILLEGAL_CHECK_SUM);
            }
        }

        // Protocol version.
        if (!VersionAssist.validate(datagram, protocolClass)) {
            throw new DecodeException(DecodeError.PROTOCOL_VERSION_NOT_MATCH);
        }

        TypeAssist assist = assists.computeIfAbsent(protocolClass, c -> TypeAssist.of(c));
        List<DecodeContext> contexts = assist.toDecodeContexts(datagram);

        contexts.parallelStream()
                .forEach(c -> {
                    TypeAssist a = c.getTypeAssist();
                    Function<DecodeContext, ?> func = DecoderFactory.getDecoder(
                            a.getDecoderClass(),
                            a.getDecodeFormula());
                    Object value = func.apply(c);
                    Object o = c.getObject();
                    a.setValue(o, value);
                });

        return assist.getObject(protocolClass);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object serialized object
     * @return binary datagram.
     */
    public static byte[] toByteArray(@NonNull Object object) {
        return toByteArray(object, true);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object serialized object
     * @param enableCompress enable compress
     * @return binary datagram.
     */
    public static byte[] toByteArray(@NonNull Object object, boolean enableCompress) {
        TypeAssist assist = assists.computeIfAbsent(object.getClass(), c -> TypeAssist.of(c));
        int length = assist.getMaxLength();
        length += CheckerUtils.getSize(object.getClass());
        length += VersionAssist.getSize(object.getClass());

        return toByteArray(object, length, enableCompress);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object serialized object
     * @param length the length of the datagram.
     * @return binary datagram.
     */
    public static byte[] toByteArray(@NonNull Object object, int length) {
        return toByteArray(object, length, true);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object         serialized object
     * @param length         the length of the datagram.
     * @param enableCompress enable compress or not
     * @return binary datagram.
     */
    public static byte[] toByteArray(@NonNull Object object, int length, boolean enableCompress) {
        byte[] datagram = new byte[length];

        TypeAssist assist = assists.computeIfAbsent(object.getClass(), c -> TypeAssist.of(c));
        List<EncodeContext> contexts = assist.toEncodeContexts(object, datagram);

        contexts.stream()
                .forEach(c -> {
                    if (c.getTypeAssist().getEncodeFormula() != null) {
                        Object o = EncoderFactory.getFormula(c.getTypeAssist().getEncodeFormula())
                                .apply(c.getValue());
                        c.setValue(o);
                    }

                    Consumer<EncodeContext> consumer = EncoderFactory.getEncoder(c.getTypeAssist().getEncoderClass());
                    consumer.accept(c);
                });

        // Protocol version.
        VersionAssist.encode(datagram, object.getClass());

        // Check sum.
        if (object.getClass().isAnnotationPresent(CheckSum.class)) {
            CheckSum checkSum = object.getClass().getAnnotation(CheckSum.class);
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
}
