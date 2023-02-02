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
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Java mapper.
 *
 * @author Deng Ran
 * @since 3.7.1
 */
public class JavaTypeMapper {
    protected static Map<Class, Class> map = new HashMap<>();

    static {
        map.put(BoolType.class, Boolean.class);
        map.put(BoolArrayType.class, boolean[].class);
        map.put(AsciiType.class, Character.class);
        map.put(AsciiArrayType.class, char[].class);
        map.put(CharType.class, Character.class);
        map.put(CharArrayType.class, char[].class);
        map.put(Int8Type.class, Integer.class);
        map.put(Int8ArrayType.class, int[].class);
        map.put(BinaryType.class, byte[].class);
        map.put(Int16Type.class, Integer.class);
        map.put(Int16ArrayType.class, int[].class);
        map.put(Int32Type.class, Integer.class);
        map.put(Int32ArrayType.class, int[].class);
        map.put(Int64Type.class, Long.class);
        map.put(Int64ArrayType.class, long[].class);
        map.put(UInt8Type.class, Integer.class);
        map.put(UInt8ArrayType.class, int[].class);
        map.put(UInt16Type.class, Integer.class);
        map.put(UInt16ArrayType.class, int[].class);
        map.put(UInt32Type.class, Long.class);
        map.put(UInt32ArrayType.class, long[].class);
        map.put(UInt64Type.class, BigInteger.class);
        map.put(UInt64ArrayType.class, BigInteger[].class);
        map.put(FloatType.class, Float.class);
        map.put(FloatArrayType.class, float[].class);
        map.put(DoubleType.class, Double.class);
        map.put(DoubleArrayType.class, double[].class);
        map.put(TimeType.class, Date.class);
        map.put(StringType.class, String.class);
        map.put(EnumType.class, Enum.class);
    }

    public static Class get(Class<? extends Annotation> dataTypeAnnotationClass) {
        if (map.containsKey(dataTypeAnnotationClass)) {
            return map.get(dataTypeAnnotationClass);
        } else {
            throw new ResolveException("Data type of %s is not supported.");
        }
    }
}
