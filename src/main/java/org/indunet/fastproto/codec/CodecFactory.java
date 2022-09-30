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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
        codecMap.put(Int16Type.class, new ConcurrentHashMap<>());
        codecMap.put(Int32Type.class, new ConcurrentHashMap<>());
        codecMap.put(Int64Type.class, new ConcurrentHashMap<>());
        codecMap.put(UInt8Type.class, new ConcurrentHashMap<>());
        codecMap.put(UInt16Type.class, new ConcurrentHashMap<>());
        codecMap.put(UInt32Type.class, new ConcurrentHashMap<>());
        codecMap.put(UInt64Type.class, new ConcurrentHashMap<>());
        codecMap.put(FloatType.class, new ConcurrentHashMap<>());
        codecMap.put(DoubleType.class, new ConcurrentHashMap<>());
        codecMap.put(BoolType.class, new ConcurrentHashMap<>());
        codecMap.put(CharType.class, new ConcurrentHashMap<>());
        codecMap.put(TimeType.class, new ConcurrentHashMap<>());
        codecMap.put(BinaryType.class, new ConcurrentHashMap<>());
        codecMap.put(EnumType.class, new ConcurrentHashMap<>());
        codecMap.put(StringType.class, new ConcurrentHashMap<>());

        val byteCodec = new ByteCodec();
        val int8Codec = new Int8Codec();
        codecMap.get(Int8Type.class).put(byte.class, byteCodec);
        codecMap.get(Int8Type.class).put(Byte.class, byteCodec);
        codecMap.get(Int8Type.class).put(int.class, int8Codec);
        codecMap.get(Int8Type.class).put(Integer.class, int8Codec);

        val shortCodec = new ShortCodec();
        val int16Codec = new Int16Codec();
        codecMap.get(Int16Type.class).put(short.class, shortCodec);
        codecMap.get(Int16Type.class).put(Short.class, shortCodec);
        codecMap.get(Int16Type.class).put(int.class, int16Codec);
        codecMap.get(Int16Type.class).put(Integer.class, int16Codec);

        val int32Codec = new Int32Codec();
        codecMap.get(Int32Type.class).put(int.class, int32Codec);
        codecMap.get(Int32Type.class).put(Integer.class, int32Codec);

        val int64Codec = new Int64Codec();
        codecMap.get(Int64Type.class).put(long.class, int64Codec);
        codecMap.get(Int64Type.class).put(Long.class, int64Codec);

        val uint8Codec = new UInt8Codec();
        codecMap.get(UInt8Type.class).put(int.class, uint8Codec);
        codecMap.get(UInt8Type.class).put(Integer.class, uint8Codec);

        val uint16Codec = new UInt16Codec();
        codecMap.get(UInt16Type.class).put(int.class, uint16Codec);
        codecMap.get(UInt16Type.class).put(Integer.class, uint16Codec);

        val uint32Codec = new UInt32Codec();
        codecMap.get(UInt32Type.class).put(long.class, uint32Codec);
        codecMap.get(UInt32Type.class).put(Long.class, uint32Codec);

        val uint64Codec = new UInt64Codec();
        codecMap.get(UInt64Type.class).put(BigInteger.class, uint64Codec);

        val floatCodec = new FloatCodec();
        codecMap.get(FloatType.class).put(float.class, floatCodec);
        codecMap.get(FloatType.class).put(Float.class, floatCodec);

        val doubleCodec = new DoubleCodec();
        codecMap.get(DoubleType.class).put(double.class, doubleCodec);
        codecMap.get(DoubleType.class).put(Double.class, doubleCodec);

        val boolCodec = new BoolCodec();
        codecMap.get(BoolType.class).put(boolean.class, boolCodec);
        codecMap.get(BoolType.class).put(Boolean.class, boolCodec);

        val charCodec = new CharCodec();
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

        val binaryCodec = new BinaryCodec();
        codecMap.get(BinaryType.class).put(byte[].class, binaryCodec);
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
