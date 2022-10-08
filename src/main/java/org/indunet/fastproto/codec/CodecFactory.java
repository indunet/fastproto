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
import org.indunet.fastproto.annotation.type.*;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.DecodingException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Codec factory.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class CodecFactory {
    protected static ConcurrentHashMap<Class, ConcurrentHashMap<Class, Codec>> codecMap = new ConcurrentHashMap<>();
    protected static ConcurrentHashMap<Class<? extends Function>, Function> formulas = new ConcurrentHashMap<>();

    static {
        codecMap.put(Int8Type.class, new ConcurrentHashMap<>());
        codecMap.put(Int8ArrayType.class, new ConcurrentHashMap<>());
        codecMap.put(BinaryType.class, new ConcurrentHashMap<>());
        codecMap.put(Int16Type.class, new ConcurrentHashMap<>());
        codecMap.put(Int16ArrayType.class, new ConcurrentHashMap<>());
        codecMap.put(Int32Type.class, new ConcurrentHashMap<>());
        codecMap.put(Int32ArrayType.class, new ConcurrentHashMap<>());
        codecMap.put(Int64Type.class, new ConcurrentHashMap<>());
        codecMap.put(Int64ArrayType.class, new ConcurrentHashMap<>());
        codecMap.put(UInt8Type.class, new ConcurrentHashMap<>());
        codecMap.put(UInt8ArrayType.class, new ConcurrentHashMap<>());
        codecMap.put(UInt16Type.class, new ConcurrentHashMap<>());
        codecMap.put(UInt16ArrayType.class, new ConcurrentHashMap<>());
        codecMap.put(UInt32Type.class, new ConcurrentHashMap<>());
        codecMap.put(UInt32ArrayType.class, new ConcurrentHashMap<>());
        codecMap.put(UInt64Type.class, new ConcurrentHashMap<>());
        codecMap.put(UInt64ArrayType.class, new ConcurrentHashMap<>());
        codecMap.put(FloatType.class, new ConcurrentHashMap<>());
        codecMap.put(FloatArrayType.class, new ConcurrentHashMap<>());
        codecMap.put(DoubleType.class, new ConcurrentHashMap<>());
        codecMap.put(DoubleArrayType.class, new ConcurrentHashMap<>());
        codecMap.put(BoolType.class, new ConcurrentHashMap<>());
        codecMap.put(CharType.class, new ConcurrentHashMap<>());
        codecMap.put(TimeType.class, new ConcurrentHashMap<>());
        codecMap.put(EnumType.class, new ConcurrentHashMap<>());
        codecMap.put(StringType.class, new ConcurrentHashMap<>());

        val byteCodec = new ByteCodec();
        val binaryCodec = new BinaryCodec();
        codecMap.get(Int8Type.class).put(byte.class, byteCodec);
        codecMap.get(Int8Type.class).put(Byte.class, byteCodec);
        codecMap.get(BinaryType.class).put(byte[].class, binaryCodec);
        codecMap.get(BinaryType.class).put(Byte[].class, binaryCodec.new WrapperCodec());

        val int8Codec = new Int8Codec();
        val int8ArrayCodec = new Int8ArrayCodec();
        codecMap.get(Int8Type.class).put(int.class, int8Codec);
        codecMap.get(Int8Type.class).put(Integer.class, int8Codec);
        codecMap.get(Int8ArrayType.class).put(byte[].class, binaryCodec);
        codecMap.get(Int8ArrayType.class).put(int[].class, int8ArrayCodec);
        codecMap.get(Int8ArrayType.class).put(Integer[].class, int8ArrayCodec.new WrapperCodec());



        val shortCodec = new ShortCodec();
        val int16Codec = new Int16Codec();
        val shortArrayCodec = new ShortArrayCodec();
        val int16ArrayCodec = new Int16ArrayCodec();
        codecMap.get(Int16Type.class).put(short.class, shortCodec);
        codecMap.get(Int16Type.class).put(Short.class, shortCodec);
        codecMap.get(Int16Type.class).put(int.class, int16Codec);
        codecMap.get(Int16Type.class).put(Integer.class, int16Codec);
        codecMap.get(Int16ArrayType.class).put(short[].class, shortArrayCodec);
        codecMap.get(Int16ArrayType.class).put(int[].class, int16ArrayCodec);
        codecMap.get(Int16ArrayType.class).put(Integer[].class, int16ArrayCodec.new WrapperCodec());
        codecMap.get(Int16ArrayType.class).put(Short[].class, shortArrayCodec.new WrapperCodec());

        val int32Codec = new Int32Codec();
        val int32ArrayCodec = new Int32ArrayCodec();
        codecMap.get(Int32Type.class).put(int.class, int32Codec);
        codecMap.get(Int32Type.class).put(Integer.class, int32Codec);
        codecMap.get(Int32ArrayType.class).put(int[].class, int32ArrayCodec);
        codecMap.get(Int32ArrayType.class).put(Integer[].class, int32ArrayCodec.new WrapperCodec());

        val int64Codec = new Int64Codec();
        val int64ArrayCodec = new Int64ArrayCodec();
        codecMap.get(Int64Type.class).put(long.class, int64Codec);
        codecMap.get(Int64Type.class).put(Long.class, int64Codec);
        codecMap.get(Int64ArrayType.class).put(long[].class, int64ArrayCodec);
        codecMap.get(Int64ArrayType.class).put(Long[].class, int64ArrayCodec.new WrapperCodec());

        val uint8Codec = new UInt8Codec();
        val uint8ArrayCodec = new UInt8ArrayCodec();
        codecMap.get(UInt8Type.class).put(int.class, uint8Codec);
        codecMap.get(UInt8Type.class).put(Integer.class, uint8Codec);
        codecMap.get(UInt8ArrayType.class).put(int[].class, uint8ArrayCodec);
        codecMap.get(UInt8ArrayType.class).put(Integer[].class, uint8ArrayCodec.new WrapperCodec());

        val uint16Codec = new UInt16Codec();
        val uint16ArrayCodec = new UInt16ArrayCodec();
        codecMap.get(UInt16Type.class).put(int.class, uint16Codec);
        codecMap.get(UInt16Type.class).put(Integer.class, uint16Codec);
        codecMap.get(UInt16ArrayType.class).put(int[].class, uint16ArrayCodec);
        codecMap.get(UInt16ArrayType.class).put(Integer[].class, uint16ArrayCodec.new WrapperCodec());

        val uint32Codec = new UInt32Codec();
        val uint32ArrayCodec = new UInt32ArrayCodec();
        codecMap.get(UInt32Type.class).put(long.class, uint32Codec);
        codecMap.get(UInt32Type.class).put(Long.class, uint32Codec);
        codecMap.get(UInt32ArrayType.class).put(long[].class, uint32ArrayCodec);
        codecMap.get(UInt32ArrayType.class).put(Long[].class, uint32ArrayCodec.new WrapperCodec());

        val uint64Codec = new UInt64Codec();
        val uint64ArrayCodec = new UInt64ArrayCodec();
        codecMap.get(UInt64Type.class).put(BigInteger.class, uint64Codec);
        codecMap.get(UInt64ArrayType.class).put(BigInteger[].class, uint64ArrayCodec);

        val floatCodec = new FloatCodec();
        val floatArrayCodec = new FloatArrayCodec();
        codecMap.get(FloatType.class).put(float.class, floatCodec);
        codecMap.get(FloatType.class).put(Float.class, floatCodec);
        codecMap.get(FloatArrayType.class).put(float[].class, floatArrayCodec);
        codecMap.get(FloatArrayType.class).put(Float[].class, floatArrayCodec.new WrapperCodec());

        val doubleCodec = new DoubleCodec();
        val doubleArrayCodec = new DoubleArrayCodec();
        codecMap.get(DoubleType.class).put(double.class, doubleCodec);
        codecMap.get(DoubleType.class).put(Double.class, doubleCodec);
        codecMap.get(DoubleArrayType.class).put(double[].class, doubleArrayCodec);
        codecMap.get(DoubleArrayType.class).put(Double[].class, doubleArrayCodec.new WrapperCodec());
        codecMap.get(DoubleArrayType.class).put(List.class, doubleArrayCodec.new CollectionCodec());

        val boolCodec = new BoolCodec();
        codecMap.get(BoolType.class).put(boolean.class, boolCodec);
        codecMap.get(BoolType.class).put(Boolean.class, boolCodec);

        val charCodec = new AsciiCodec();
        codecMap.get(CharType.class).put(char.class, charCodec);
        codecMap.get(CharType.class).put(Character.class, charCodec);

        val dateCodec = new DateCodec();
        val timestampCodec = new TimestampCodec();
        val calendarCodec = new CalendarCodec();
        val instantCodec = new InstantCodec();
        codecMap.get(TimeType.class).put(Date.class, dateCodec);
        codecMap.get(TimeType.class).put(Timestamp.class, timestampCodec);
        codecMap.get(TimeType.class).put(Calendar.class, calendarCodec);
        codecMap.get(TimeType.class).put(Instant.class, instantCodec);

        val stringCodec = new StringCodec();
        val stringBufferCodec = new StringBufferCodec();
        val stringBuilderCodec = new StringBuilderCodec();
        codecMap.get(StringType.class).put(String.class, stringCodec);
        codecMap.get(StringType.class).put(StringBuffer.class, stringBufferCodec);
        codecMap.get(StringType.class).put(StringBuilder.class, stringBuilderCodec);

        val enumCodec = new EnumCodec<>();
        codecMap.get(EnumType.class).put(Enum.class, enumCodec);
    }

    public static boolean isSupported(Type type) {
        return codecMap.values()
                .stream()
                .flatMap(m -> m.keySet().stream())
                .anyMatch(type::equals);
    }

    public static Codec createCodec(Class type, Class fieldClass) {
        if (!codecMap.containsKey(type)) {
            throw new CodecException(String.format("%s is not supported.", type.toString()));
        }

        val map = codecMap.get(type);

        if (Enum.class.isAssignableFrom(fieldClass)) {
            return map.get(Enum.class);
        } else if (!map.containsKey(fieldClass)) {
            throw new CodecException(String.format("%s cannot be used on %s", type.getSimpleName(), fieldClass));
        } else {
            return map.get(fieldClass);
        }
    }

    public static <T, R> Function<T, R> createFormula(Class<? extends Function> clazz) {
        return formulas.computeIfAbsent(clazz, c -> {
            try {
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new DecodingException(
                        MessageFormat.format(CodecError.FAIL_INITIALIZING_DECODE_FORMULA.getMessage(), clazz.getName()), e);
            }
        });
    }

    public static Function<byte[], ?> createDecoder(CodecContext context, Class<? extends Function> clazz) {
        if (clazz != null) {
            val type = Arrays.stream(clazz.getGenericInterfaces())
                    .filter(i -> i instanceof ParameterizedType)
                    .map(i -> ((ParameterizedType) i).getActualTypeArguments())
                    .map(a -> a[0])
                    .findAny()
                    .get();

            Function<byte[], ?> func = (byte[] bytes) -> createCodec(context.getDataTypeAnnotation().annotationType(), (Class) type)
                    .decode(context, bytes);

            return func.andThen(createFormula(clazz));
        } else {
            return (byte[] bytes) -> createCodec(context.getDataTypeAnnotation().annotationType(), context.getFieldType())
                    .decode(context, bytes);
        }
    }

    public static BiConsumer<byte[], ? super Object> createEncoder(CodecContext context, Class<? extends Function> clazz) {
        if (clazz != null) {
            val type = Arrays.stream(clazz.getGenericInterfaces())
                    .filter(i -> i instanceof ParameterizedType)
                    .map(i -> ((ParameterizedType) i).getActualTypeArguments())
                    .map(a -> a[1])
                    .findAny()
                    .get();

            return (bytes, value) -> createCodec(context.getDataTypeAnnotation().annotationType(), (Class) type)
                    .encode(context, bytes, createFormula(clazz).apply(value));
        } else {
            return (bytes, value) -> createCodec(context.getDataTypeAnnotation().annotationType(), context.getFieldType())
                    .encode(context, bytes, value);
        }
    }
}
