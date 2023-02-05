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

package org.indunet.fastproto.mapper;

import lombok.val;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.codec.*;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Codec mapper.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class CodecMapper {
    protected static ConcurrentHashMap<Class, Map<Predicate<Type>, Codec>> codecMap = new ConcurrentHashMap<>();
    protected static ConcurrentHashMap<Class<? extends Function>, Function> formulas = new ConcurrentHashMap<>();

    static {
        codecMap.put(Int8Type.class, new HashMap<>());
        codecMap.put(Int8ArrayType.class, new HashMap<>());
        codecMap.put(BinaryType.class, new HashMap<>());
        codecMap.put(Int16Type.class, new HashMap<>());
        codecMap.put(Int16ArrayType.class, new HashMap<>());
        codecMap.put(Int32Type.class, new HashMap<>());
        codecMap.put(Int32ArrayType.class, new HashMap<>());
        codecMap.put(Int64Type.class, new HashMap<>());
        codecMap.put(Int64ArrayType.class, new HashMap<>());
        codecMap.put(UInt8Type.class, new HashMap<>());
        codecMap.put(UInt8ArrayType.class, new HashMap<>());
        codecMap.put(UInt16Type.class, new HashMap<>());
        codecMap.put(UInt16ArrayType.class, new HashMap<>());
        codecMap.put(UInt32Type.class, new HashMap<>());
        codecMap.put(UInt32ArrayType.class, new HashMap<>());
        codecMap.put(UInt64Type.class, new HashMap<>());
        codecMap.put(UInt64ArrayType.class, new HashMap<>());
        codecMap.put(FloatType.class, new HashMap<>());
        codecMap.put(FloatArrayType.class, new HashMap<>());
        codecMap.put(DoubleType.class, new HashMap<>());
        codecMap.put(DoubleArrayType.class, new HashMap<>());
        codecMap.put(BoolType.class, new HashMap<>());
        codecMap.put(BoolArrayType.class, new HashMap<>());
        codecMap.put(AsciiType.class, new HashMap<>());
        codecMap.put(AsciiArrayType.class, new HashMap<>());
        codecMap.put(CharType.class, new HashMap<>());
        codecMap.put(CharArrayType.class, new HashMap<>());
        codecMap.put(TimeType.class, new HashMap<>());
        codecMap.put(EnumType.class, new HashMap<>());
        codecMap.put(StringType.class, new HashMap<>());

        BiFunction<Type, Class, Boolean> collectionType = (t, c) -> t instanceof ParameterizedType
                && Collection.class.isAssignableFrom((Class) ((ParameterizedType) t).getRawType())
                && ((ParameterizedType) t).getActualTypeArguments()[0].equals(c);

        val byteCodec = new ByteCodec();
        val binaryCodec = new BinaryCodec();
        codecMap.get(Int8Type.class).put(
                c -> c.equals(byte.class) || c.equals(Byte.class), byteCodec);
        codecMap.get(BinaryType.class).put(c -> c.equals(byte[].class), binaryCodec);
        codecMap.get(BinaryType.class).put(c -> c.equals(Byte[].class), binaryCodec.new WrapperCodec());
        codecMap.get(BinaryType.class).put(t -> collectionType.apply(t, Byte.class), binaryCodec.new CollectionCodec());

        val int8Codec = new Int8Codec();
        val int8ArrayCodec = new Int8ArrayCodec();
        codecMap.get(Int8Type.class).put(
                c -> c.equals(int.class) || c.equals(Integer.class), int8Codec);
        codecMap.get(Int8ArrayType.class).put(c -> c.equals(byte[].class), binaryCodec);
        codecMap.get(Int8ArrayType.class).put(c -> c.equals(int[].class), int8ArrayCodec);
        codecMap.get(Int8ArrayType.class).put(c -> c.equals(Integer[].class), int8ArrayCodec.new WrapperCodec());
        codecMap.get(Int8ArrayType.class).put(t -> collectionType.apply(t, Integer.class), int8ArrayCodec.new CollectionCodec());

        val shortCodec = new ShortCodec();
        val int16Codec = new Int16Codec();
        val shortArrayCodec = new ShortArrayCodec();
        val int16ArrayCodec = new Int16ArrayCodec();
        codecMap.get(Int16Type.class).put(c -> c.equals(short.class) || c.equals(Short.class), shortCodec);
        codecMap.get(Int16Type.class).put(c -> c.equals(int.class) || c.equals(Integer.class), int16Codec);
        codecMap.get(Int16ArrayType.class).put(c -> c.equals(short[].class), shortArrayCodec);
        codecMap.get(Int16ArrayType.class).put(c -> c.equals(int[].class), int16ArrayCodec);
        codecMap.get(Int16ArrayType.class).put(c -> c.equals(Integer[].class), int16ArrayCodec.new WrapperCodec());
        codecMap.get(Int16ArrayType.class).put(t -> collectionType.apply(t, Integer.class), int16ArrayCodec.new CollectionCodec());
        codecMap.get(Int16ArrayType.class).put(c -> c.equals(Short[].class), shortArrayCodec.new WrapperCodec());
        codecMap.get(Int16ArrayType.class).put(t -> collectionType.apply(t, Short.class), shortArrayCodec.new CollectionCodec());

        val int32Codec = new Int32Codec();
        val int32ArrayCodec = new Int32ArrayCodec();
        codecMap.get(Int32Type.class).put(c -> c.equals(int.class) || c.equals(Integer.class), int32Codec);
        codecMap.get(Int32ArrayType.class).put(c -> c.equals(int[].class), int32ArrayCodec);
        codecMap.get(Int32ArrayType.class).put(c -> c.equals(Integer[].class), int32ArrayCodec.new WrapperCodec());
        codecMap.get(Int32ArrayType.class).put(t -> collectionType.apply(t, Integer.class), int32ArrayCodec.new CollectionCodec());

        val int64Codec = new Int64Codec();
        val int64ArrayCodec = new Int64ArrayCodec();
        codecMap.get(Int64Type.class).put(c -> c.equals(long.class) || c.equals(Long.class), int64Codec);
        codecMap.get(Int64ArrayType.class).put(c -> c.equals(long[].class), int64ArrayCodec);
        codecMap.get(Int64ArrayType.class).put(c -> c.equals(Long[].class), int64ArrayCodec.new WrapperCodec());
        codecMap.get(Int64ArrayType.class).put(t -> collectionType.apply(t, Long.class), int64ArrayCodec.new CollectionCodec());

        val uint8Codec = new UInt8Codec();
        val uint8ArrayCodec = new UInt8ArrayCodec();
        codecMap.get(UInt8Type.class).put(c -> c.equals(int.class) || c.equals(Integer.class), uint8Codec);
        codecMap.get(UInt8ArrayType.class).put(c -> c.equals(int[].class), uint8ArrayCodec);
        codecMap.get(UInt8ArrayType.class).put(c -> c.equals(Integer[].class), uint8ArrayCodec.new WrapperCodec());
        codecMap.get(UInt8ArrayType.class).put(t -> collectionType.apply(t, Integer.class), uint8ArrayCodec.new CollectionCodec());

        val uint16Codec = new UInt16Codec();
        val uint16ArrayCodec = new UInt16ArrayCodec();
        codecMap.get(UInt16Type.class).put(c -> c.equals(int.class) || c.equals(Integer.class), uint16Codec);
        codecMap.get(UInt16ArrayType.class).put(c -> c.equals(int[].class), uint16ArrayCodec);
        codecMap.get(UInt16ArrayType.class).put(c -> c.equals(Integer[].class), uint16ArrayCodec.new WrapperCodec());
        codecMap.get(UInt16ArrayType.class).put(t -> collectionType.apply(t, Integer.class), uint16ArrayCodec.new CollectionCodec());

        val uint32Codec = new UInt32Codec();
        val uint32ArrayCodec = new UInt32ArrayCodec();
        codecMap.get(UInt32Type.class).put(c -> c.equals(long.class) || c.equals(Long.class), uint32Codec);
        codecMap.get(UInt32ArrayType.class).put(c -> c.equals(long[].class), uint32ArrayCodec);
        codecMap.get(UInt32ArrayType.class).put(c -> c.equals(Long[].class), uint32ArrayCodec.new WrapperCodec());
        codecMap.get(UInt32ArrayType.class).put(t -> collectionType.apply(t, Long.class), uint32ArrayCodec.new CollectionCodec());

        val uint64Codec = new UInt64Codec();
        val uint64ArrayCodec = new UInt64ArrayCodec();
        codecMap.get(UInt64Type.class).put(c -> c.equals(BigInteger.class), uint64Codec);
        codecMap.get(UInt64ArrayType.class).put(c -> c.equals(BigInteger[].class), uint64ArrayCodec);
        codecMap.get(UInt64ArrayType.class).put(t -> collectionType.apply(t, BigInteger.class), uint64ArrayCodec.new CollectionCodec());

        val floatCodec = new FloatCodec();
        val floatArrayCodec = new FloatArrayCodec();
        codecMap.get(FloatType.class).put(c -> c.equals(float.class) || c.equals(Float.class), floatCodec);
        codecMap.get(FloatArrayType.class).put(c -> c.equals(float[].class), floatArrayCodec);
        codecMap.get(FloatArrayType.class).put(c -> c.equals(Float[].class), floatArrayCodec.new WrapperCodec());
        codecMap.get(FloatArrayType.class).put(t -> collectionType.apply(t, Float.class), floatArrayCodec.new CollectionCodec());

        val doubleCodec = new DoubleCodec();
        val doubleArrayCodec = new DoubleArrayCodec();
        codecMap.get(DoubleType.class).put(c -> c.equals(double.class) || c.equals(Double.class), doubleCodec);
        codecMap.get(DoubleArrayType.class).put(c -> c.equals(double[].class), doubleArrayCodec);
        codecMap.get(DoubleArrayType.class).put(c -> c.equals(Double[].class), doubleArrayCodec.new WrapperCodec());
        codecMap.get(DoubleArrayType.class).put(t -> collectionType.apply(t, Double.class), doubleArrayCodec.new CollectionCodec());

        val boolCodec = new BoolCodec();
        val boolArrayCodec = new BoolArrayCodec();
        codecMap.get(BoolType.class).put(c -> c.equals(boolean.class) || c.equals(Boolean.class), boolCodec);
        codecMap.get(BoolArrayType.class).put(c -> c.equals(boolean[].class), boolArrayCodec);
        codecMap.get(BoolArrayType.class).put(c -> c.equals(Boolean[].class), boolArrayCodec.new WrapperCodec());
        codecMap.get(BoolArrayType.class).put(t -> collectionType.apply(t, Boolean.class), boolArrayCodec.new CollectionCodec());

        val asciiCodec = new AsciiCodec();
        val asciiArrayCodec = new AsciiArrayCodec();
        codecMap.get(AsciiType.class).put(c -> c.equals(char.class) || c.equals(Character.class), asciiCodec);
        codecMap.get(AsciiArrayType.class).put(c -> c.equals(char[].class) || c.equals(Character.class), asciiArrayCodec);
        codecMap.get(AsciiArrayType.class).put(c -> c.equals(Character[].class) || c.equals(Character.class), asciiArrayCodec.new WrapperCodec());
        codecMap.get(AsciiArrayType.class).put(t -> collectionType.apply(t, Character.class), asciiArrayCodec.new CollectionCodec());

        val charCodec = new CharCodec();
        val charArrayCodec = new CharArrayCodec();
        codecMap.get(CharType.class).put(c -> c.equals(char.class) || c.equals(Character.class), charCodec);
        codecMap.get(CharArrayType.class).put(c -> c.equals(char[].class) || c.equals(Character.class), charArrayCodec);
        codecMap.get(CharArrayType.class).put(c -> c.equals(Character[].class) || c.equals(Character.class), charArrayCodec.new WrapperCodec());
        codecMap.get(CharArrayType.class).put(t -> collectionType.apply(t, Character.class), charArrayCodec.new CollectionCodec());

        val dateCodec = new DateCodec();
        codecMap.get(TimeType.class).put(c -> c.equals(Date.class), dateCodec);
        codecMap.get(TimeType.class).put(c -> c.equals(Timestamp.class), dateCodec.new TimestampCodec());
        codecMap.get(TimeType.class).put(c -> c.equals(Calendar.class), dateCodec.new CalendarCodec());
        codecMap.get(TimeType.class).put(c -> c.equals(Instant.class), dateCodec.new InstantCodec());
        codecMap.get(TimeType.class).put(c -> c.equals(LocalDateTime.class), dateCodec.new LocalDateTimeCodec());

        val stringCodec = new StringCodec();
        codecMap.get(StringType.class).put(c -> c.equals(String.class), stringCodec);
        codecMap.get(StringType.class).put(c -> c.equals(StringBuffer.class), stringCodec.new StringBufferCodec());
        codecMap.get(StringType.class).put(c -> c.equals(StringBuilder.class), stringCodec.new StringBuilderCodec());

        val enumCodec = new EnumCodec<>();
        codecMap.get(EnumType.class).put(t -> Enum.class.isAssignableFrom((Class) t), enumCodec);
    }

    public static boolean isSupported(Type type) {
        return codecMap.values()
                .stream()
                .flatMap(m -> m.keySet().stream())
                .anyMatch(p -> p.test((Class) type));
    }

    public static Codec getCodec(Class type, Type fieldClass) {
        if (!codecMap.containsKey(type)) {
            throw new CodecException(String.format("%s is not supported.", type.toString()));
        }

        val map = codecMap.get(type);

        return map.entrySet().stream()
                .filter(e -> e.getKey().test(fieldClass))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new CodecException(String.format("%s cannot be used on %s", type.getSimpleName(), fieldClass)));
    }

    public static <T, R> Function<T, R> getFormula(Class<? extends Function> clazz) {
        return formulas.computeIfAbsent(clazz, c -> {
            try {
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new DecodingException(String.format("Fail initializing decoding formula %s", clazz.getName()), e);
            }
        });
    }

    public static Class getDataTypeAnnotationClass(Class fieldType) {
        return codecMap.entrySet().stream()
                .filter(e -> e.getValue().keySet().stream()
                        .anyMatch(p -> p.test(fieldType)))
                .map(Map.Entry::getKey)
                .findAny()
                .orElseThrow(() -> new ResolveException(
                        String.format("%s is not supported", fieldType.getName())));
    }

    public static Function<ByteBufferInputStream, ?> getDecoder(CodecContext context, Class<? extends Function> clazz) {
        if (clazz != null) {
            val type = Arrays.stream(clazz.getGenericInterfaces())
                    .filter(i -> i instanceof ParameterizedType)
                    .map(i -> ((ParameterizedType) i).getActualTypeArguments())
                    .map(a -> a[0])
                    .findAny()
                    .get();

            Function<ByteBufferInputStream, ?> func = (ByteBufferInputStream inputStream) -> getCodec(context.getDataTypeAnnotation().annotationType(), (Class) type)
                    .decode(context, inputStream);

            return func.andThen(getFormula(clazz));
        } else {
            return (ByteBufferInputStream inputStream) -> getCodec(context.getDataTypeAnnotation().annotationType(), context.getField().getGenericType())
                    .decode(context, inputStream);
        }
    }

    public static Function<ByteBufferInputStream, ?> getDefaultDecoder(CodecContext context, Class type) {
        return (inputStream) -> getCodec(context.getDataTypeAnnotation().annotationType(), type)
                .decode(context, inputStream);
    }

    public static BiConsumer<ByteBufferOutputStream, ? super Object> getEncoder(CodecContext context, Class<? extends Function> clazz) {
        if (clazz != null) {
            val type = Arrays.stream(clazz.getGenericInterfaces())
                    .filter(i -> i instanceof ParameterizedType)
                    .map(i -> ((ParameterizedType) i).getActualTypeArguments())
                    .map(a -> a[1])
                    .findAny()
                    .get();

            return (outputStream, value) -> getCodec(context.getDataTypeAnnotation().annotationType(), (Class) type)
                    .encode(context, outputStream, getFormula(clazz).apply(value));
        } else {
            return (outputStream, value) -> getCodec(context.getDataTypeAnnotation().annotationType(), context.getField().getGenericType())
                    .encode(context, outputStream, value);
        }
    }

    public static BiConsumer<ByteBufferOutputStream, ? super Object> getDefaultEncoder(CodecContext context, Class type) {
        return (outputStream, value) -> getCodec(context.getDataTypeAnnotation().annotationType(), type)
                .encode(context, outputStream, value);
    }
}
