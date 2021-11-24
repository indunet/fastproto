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

package org.indunet.fastproto.util;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.decoder.TypeDecoder;
import org.indunet.fastproto.encoder.TypeEncoder;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CodecException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Type utils.
 *
 * @author Deng Ran
 * @since 2.0.0
 */
public class TypeUtils {
    protected final static String SIZE_NAME = "SIZE";
    protected final static String BYTE_OFFSET_NAME = "value";
    protected final static String BIT_OFFSET_NAME = "bitOffset";
    protected final static String LENGTH_NAME = "length";
    protected final static String PROTOCOL_TYPES_NAME = "PROTOCOL_TYPES";
    protected final static String ENCODE_FORMULA_NAME = "encodingFormula";
    protected final static String DECODE_FORMULA_NAME = "decodingFormula";
    protected final static String JAVA_TYPES_NAME = "JAVA_TYPES";

    public static Type wrapperClass(@NonNull String name) {
        switch (name) {
            case "boolean":
                return Boolean.class;
            case "byte":
                return Byte.class;
            case "char":
                return Character.class;
            case "short":
                return Short.class;
            case "int":
                return Integer.class;
            case "long":
                return Long.class;
            case "float":
                return Float.class;
            case "double":
                return Double.class;
            default:
                throw new CodecException(
                        MessageFormat.format(CodecError.UNSUPPORTED_TYPE.getMessage(), name));
        }
    }

    public static Class<? extends TypeDecoder> decoderClass(@NonNull Annotation typeAnnotation) {
        return Optional.of(typeAnnotation.annotationType())
                .map(t -> t.getAnnotation(Decoder.class))
                .map(Decoder::value)
                .orElse(null);
    }

    public static Class<? extends TypeEncoder> encoderClass(@NonNull Annotation typeAnnotation) {
        return Optional.of(typeAnnotation.annotationType())
                .map(t -> t.getAnnotation(Encoder.class))
                .map(Encoder::value)
                .orElse(null);
    }

    public static Class<? extends Function> encodingFormula(@NonNull Annotation typeAnnotation) {
        return formula(typeAnnotation, ENCODE_FORMULA_NAME);
    }

    public static Class<? extends Function> decodingFormula(@NonNull Annotation typeAnnotation) {
        return formula(typeAnnotation, DECODE_FORMULA_NAME);
    }

    @SneakyThrows
    protected static Class<? extends Function> formula(@NonNull Annotation typeAnnotation, @NonNull String name) {
        val method = typeAnnotation.getClass().getMethod(name);
        val array = method.invoke(typeAnnotation);

        return Optional.of(array)
                .filter(a -> a.getClass().isArray())
                .filter(a -> Array.getLength(a) >= 1)
                .map(a -> Array.get(a, 0))
                .map(o -> (Class<? extends Function>) o)
                .orElse(null);
    }

    public static int byteOffset(@NonNull Annotation typeAnnotation) {
        try {
            return (Integer) typeAnnotation
                    .getClass()
                    .getMethod(BYTE_OFFSET_NAME)
                    .invoke(typeAnnotation);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return 0;
        }
    }

    public static int bitOffset(@NonNull Annotation typeAnnotation) {
        try {
            return (Integer) typeAnnotation
                    .getClass()
                    .getMethod(BIT_OFFSET_NAME)
                    .invoke(typeAnnotation);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return 0;
        }
    }


    public static int length(@NonNull Annotation typeAnnotation) {
        try {
            return (Integer) typeAnnotation
                    .getClass()
                    .getMethod(LENGTH_NAME)
                    .invoke(typeAnnotation);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return 0;
        }
    }

    public static int size(@NonNull Class<? extends Annotation> type) {
        try {
            return type
                    .getDeclaredField("SIZE")
                    .getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return 0;
        }
    }

    public static Object listToArray(List<?> list, Object array) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("The object must be array type.");
        }

        IntStream.range(0, list.size())
                .forEach(i -> {
                    Array.set(array, i, list.get(i));
                });

        return array;
    }
}
