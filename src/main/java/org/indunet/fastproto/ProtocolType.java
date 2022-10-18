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

import org.indunet.fastproto.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author Deng Ran
 * @since 3.2.0
 */
public interface ProtocolType {
    Class<? extends Annotation> BINARY = BinaryType.class;
    Class<? extends Annotation> BOOL = BoolType.class;
    Class<? extends Annotation> CHAR = CharType.class;
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
                            .noneMatch(m -> m.getName().equals("length"))) {
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

    Class defaultJavaType();
}
