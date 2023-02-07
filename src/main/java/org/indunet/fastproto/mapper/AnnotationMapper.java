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

import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.exception.ResolveException;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Data type annotation mapper.
 *
 * @author Deng Ran
 * @since 3.7.1
 */
public class AnnotationMapper {
    protected static Map<Predicate<Type>, Class<? extends Annotation>> map = new HashMap<>();

    static {
        BiFunction<Type, Class, Boolean> collectionType = (t, c) -> t instanceof ParameterizedType
                && Collection.class.isAssignableFrom((Class) ((ParameterizedType) t).getRawType())
                && ((ParameterizedType) t).getActualTypeArguments()[0].equals(c);

        map.put(c -> c.equals(byte.class) || c.equals(Byte.class), Int8Type.class);
        map.put(c -> c.equals(byte[].class), Int8ArrayType.class);
        map.put(c -> c.equals(Byte[].class), Int8ArrayType.class);
        map.put(t -> collectionType.apply(t, Byte.class), Int8ArrayType.class);

        map.put(c -> c.equals(short.class) || c.equals(Short.class), Int16Type.class);
        map.put(c -> c.equals(short[].class), Int16ArrayType.class);
        map.put(c -> c.equals(Short[].class), Int16ArrayType.class);
        map.put(t -> collectionType.apply(t, Short.class), Int16ArrayType.class);

        map.put(c -> c.equals(int.class) || c.equals(Integer.class), Int32Type.class);
        map.put(c -> c.equals(int[].class), Int32ArrayType.class);
        map.put(c -> c.equals(Integer[].class), Int32ArrayType.class);
        map.put(t -> collectionType.apply(t, Integer.class), Int32ArrayType.class);

        map.put(c -> c.equals(long.class) || c.equals(Long.class), Int64Type.class);
        map.put(c -> c.equals(long[].class), Int64ArrayType.class);
        map.put(c -> c.equals(Long[].class), Int64ArrayType.class);
        map.put(t -> collectionType.apply(t, Long.class), Int64ArrayType.class);

        map.put(c -> c.equals(float.class) || c.equals(Float.class), FloatType.class);
        map.put(c -> c.equals(float[].class), FloatArrayType.class);
        map.put(c -> c.equals(Float[].class), FloatArrayType.class);
        map.put(t -> collectionType.apply(t, Float.class), FloatArrayType.class);

        map.put(c -> c.equals(double.class) || c.equals(Double.class), DoubleType.class);
        map.put(c -> c.equals(double[].class) || c.equals(Double[].class), DoubleArrayType.class);
        map.put(t -> collectionType.apply(t, Double.class), DoubleArrayType.class);

        map.put(c -> c.equals(boolean.class) || c.equals(Boolean.class), BoolType.class);
        map.put(c -> c.equals(boolean[].class) || c.equals(Boolean[].class), BoolType.class);
        map.put(t -> collectionType.apply(t, Character.class), BoolArrayType.class);

        map.put(c -> c.equals(char.class) || c.equals(Character.class), AsciiType.class);
        map.put(c -> c.equals(char[].class) || c.equals(Character[].class), AsciiArrayType.class);
        map.put(t -> collectionType.apply(t, Character.class), AsciiArrayType.class);

        map.put(c -> c.equals(Date.class), TimeType.class);
        map.put(c -> c.equals(Timestamp.class), TimeType.class);
        map.put(c -> c.equals(Calendar.class), TimeType.class);
        map.put(c -> c.equals(Instant.class), TimeType.class);
        map.put(c -> c.equals(LocalDateTime.class), TimeType.class);

        map.put(c -> c.equals(String.class), StringType.class);
        map.put(c -> c.equals(StringBuffer.class), StringType.class);
        map.put(c -> c.equals(StringBuilder.class), StringType.class);

        map.put(t -> t instanceof Class && Enum.class.isAssignableFrom((Class) t), EnumType.class);
    }

    public static Class<? extends Annotation> get(Type type) {
        return map.entrySet().stream()
                .filter(e -> e.getKey().test(type))
                .map(Map.Entry::getValue)
                .findAny()
                .orElseThrow(() ->
                        new ResolveException(String.format("Type %s is not supported.", type)));
    }
}
