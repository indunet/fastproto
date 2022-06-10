/*
 * Copyright 2019-2021 indunet.org
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

import org.indunet.fastproto.annotation.type.*;
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.util.TypeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @since 3.2.0
 */
public interface ProtocolType {
    Class<? extends Annotation> BINARY = BinaryType.class;
    Class<? extends Annotation> BOOLEAN = BoolType.class;
    Class<? extends Annotation> CHAR = CharType.class;
    Class<? extends Annotation> BYTE = ByteType.class;
    Class<? extends Annotation> DOUBLE = DoubleType.class;
    Class<? extends Annotation> FLOAT = FloatType.class;
    Class<? extends Annotation> INT32 = Int32Type.class;
    Class<? extends Annotation> LONG = Int64Type.class;
    Class<? extends Annotation> SHORT = ShortType.class;
    Class<? extends Annotation> STRING = StringType.class;
    Class<? extends Annotation> TIME = TimeType.class;
    Class<? extends Annotation> INT8 = Int8Type.class;
    Class<? extends Annotation> INT16 = Int16Type.class;
    Class<? extends Annotation> UINT8 = UInt8Type.class;
    Class<? extends Annotation> UINT16 = UInt16Type.class;
    Class<? extends Annotation> UINT32 = UInt32Type.class;
    Class<? extends Annotation> UINT64 = UInt64Type.class;
    Class<? extends Annotation> ENUM = EnumType.class;
    Class<? extends Annotation> LIST = ListType.class;
    Class<? extends Annotation> ARRAY = ArrayType.class;

    static boolean isSupported(Type type) {
        return Arrays.stream(ProtocolType.class.getDeclaredFields())
                .filter(f -> f.getType() != boolean[].class)    // Filter boolean[], jacoco would add it during test.
                .map(f -> {
                    try {
                        return (Class<? extends Annotation>) f.get(null);
                    } catch (IllegalAccessException e) {
                        throw new ResolveException("Fail getting protocol type.", e);
                    }
                })
                .flatMap(t -> Arrays.stream(TypeUtils.javaTypes(t)))
                .anyMatch(t -> t == type);
    }

    static ProtocolType proxy(Annotation typeAnnotation) {
        return (ProtocolType) Proxy.newProxyInstance(ProtocolType.class.getClassLoader(), new Class<?>[]{ProtocolType.class, typeAnnotation.annotationType()}, (proxy, method, args) -> {
            switch (method.getName()) {
                case "getType":
                    return typeAnnotation.annotationType();
                case "javaTypes":
                    return typeAnnotation
                            .annotationType()
                            .getDeclaredField("ALLOWED_JAVA_TYPES")
                            .get(null);
                case "size":
                    try {
                        return typeAnnotation
                                .annotationType()
                                .getDeclaredField("SIZE")
                                .getInt(null);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        return 0;
                    }
                case "length":
                    if (!Arrays.stream(typeAnnotation.getClass().getMethods())
                            .anyMatch(m -> m.getName().equals("length"))) {
                        return 0;
                    }
                default:
                    return Arrays.stream(typeAnnotation.getClass().getMethods())
                            .filter(m -> m.getName().equals(method.getName()))
                            .findAny()
                            .get()
                            .invoke(typeAnnotation, args);
            }
        });
    }

    int value();

    int byteOffset();

    int bitOffset();

    int length();

    Class<? extends Function<?, ?>>[] decodingFormula();

    Class<? extends Function<?, ?>>[] encodingFormula();

    String description();

    String field();

    EndianPolicy[] endianPolicy();

    Class<? extends Annotation> getType();

    Type[] javaTypes();

    int size();
}
