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

import lombok.val;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.exception.ResolveException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Protocol type.
 *
 * @author Deng Ran
 * @since 3.2.0
 */
public interface ProtocolType {
    Class<? extends Annotation> BINARY = BinaryType.class;
    Class<? extends Annotation> BOOL = BoolType.class;
    Class<? extends Annotation> CHAR = AsciiType.class;
    Class<? extends Annotation> DOUBLE = DoubleType.class;
    Class<? extends Annotation> FLOAT = FloatType.class;
    Class<? extends Annotation> INT32 = Int32Type.class;
    Class<? extends Annotation> INT64 = Int64Type.class;
    Class<? extends Annotation> STRING = StringType.class;
    Class<? extends Annotation> TIME = TimeType.class;

    Class<? extends Annotation> INT8 = Int8Type.class;
    Class<? extends Annotation> INT16 = Int16Type.class;
    Class<? extends Annotation> UINT8 = UInt8Type.class;
    Class<? extends Annotation> UINT16 = UInt16Type.class;
    Class<? extends Annotation> UINT32 = UInt32Type.class;
    Class<? extends Annotation> UINT64 = UInt64Type.class;
    Class<? extends Annotation> ENUM = EnumType.class;

    static <T> T proxy(AutoType autoType, Class<T> dataTypeAnnotationClass) {
        return (T) Proxy.newProxyInstance(ProtocolType.class.getClassLoader(), new Class<?>[] {dataTypeAnnotationClass}, (proxy, method, args) -> {
            val mth = Arrays.stream(autoType.getClass().getMethods())
                    .filter(m -> m.getName().equals(method.getName()))
                    .findAny()
                    .get();

            if (Arrays.asList("offset", "byteOffset", "bitOffset", "length")
                    .contains(mth.getName())) {
                val ints = (int[]) mth.invoke(autoType, args);

                if (ints.length != 0) {
                    return ints[0];
                } else {
                    throw new ResolveException(
                            String.format("Autotype lack of property %s", mth.getName()));
                }
            } else if (Arrays.asList("endian", "charset", "name")
                    .contains(method.getName())) {
                return mth.invoke(autoType, args);
            } else if (mth.getName().equals("annotationType")) {
                return dataTypeAnnotationClass;
            } else {
                return null;
            }
        });
    }

    static ProtocolType proxy(Annotation typeAnnotation) {
        return (ProtocolType) Proxy.newProxyInstance(ProtocolType.class.getClassLoader(), new Class<?>[]{ProtocolType.class, typeAnnotation.annotationType()}, (proxy, method, args) -> {
            switch (method.getName()) {
                case "getType":
                    return typeAnnotation.annotationType();
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
                    if (Arrays.stream(typeAnnotation.getClass().getMethods())
                            .anyMatch(m -> m.getName().equals("length"))) {
                        return Arrays.stream(typeAnnotation.getClass().getMethods())
                                .filter(m -> m.getName().equals("length"))
                                .findAny()
                                .get()
                                .invoke(typeAnnotation, args);
                    } else {
                        return 0;
                    }
                case "defaultJavaType":
                    try {
                        return (Class) typeAnnotation
                                .annotationType()
                                .getDeclaredField("DEFAULT_JAVA_TYPE")
                                .get(null);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        return null;
                    }
                default:
                    if (typeAnnotation.annotationType() == BoolType.class && method.getName().equals("offset")) {
                        return Arrays.stream(typeAnnotation.getClass().getMethods())
                                .filter(m -> m.getName().equals("byteOffset"))
                                .findAny()
                                .get()
                                .invoke(typeAnnotation, args);
                    } else if (typeAnnotation.annotationType() == BoolArrayType.class && method.getName().equals("offset")) {
                        return Arrays.stream(typeAnnotation.getClass().getMethods())
                                .filter(m -> m.getName().equals("byteOffset"))
                                .findAny()
                                .get()
                                .invoke(typeAnnotation, args);
                    } else {
                        return Arrays.stream(typeAnnotation.getClass().getMethods())
                                .filter(m -> m.getName().equals(method.getName()))
                                .findAny()
                                .get()
                                .invoke(typeAnnotation, args);
                    }
            }
        });
    }

    int offset();

    int byteOffset();

    int bitOffset();

    int length();

    String field();

    EndianPolicy[] endianPolicy();

    Class<? extends Annotation> getType();

    int size();
}
