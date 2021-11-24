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
    final static Class<? extends Annotation> BINARY = BinaryType.class;
    Class<? extends Annotation> BOOLEAN = BooleanType.class;
    Class<? extends Annotation> CHARACTER = CharacterType.class;
    Class<? extends Annotation> BYTE = ByteType.class;
    Class<? extends Annotation> DOUBLE = DoubleType.class;
    Class<? extends Annotation> FLOAT = FloatType.class;
    Class<? extends Annotation> INTEGER = IntegerType.class;
    Class<? extends Annotation> LONG = LongType.class;
    Class<? extends Annotation> SHORT = ShortType.class;
    Class<? extends Annotation> STRING = StringType.class;
    Class<? extends Annotation> TIMESTAMP = TimestampType.class;
    Class<? extends Annotation> INTEGER8 = Integer8Type.class;
    Class<? extends Annotation> INTEGER16 = Integer16Type.class;
    Class<? extends Annotation> UINTEGER8 = UInteger8Type.class;
    Class<? extends Annotation> UINTEGER16 = UInteger16Type.class;
    Class<? extends Annotation> UINTEGER32 = UInteger32Type.class;
    Class<? extends Annotation> UINTEGER64 = UInteger64Type.class;
    Class<? extends Annotation> ENUM = EnumType.class;
    Class<? extends Annotation> LIST = ListType.class;
    Class<? extends Annotation> ARRAY = ArrayType.class;

    static ProtocolType proxy(Annotation typeAnnotation) {
        return (ProtocolType) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class<?>[]{ProtocolType.class, typeAnnotation.annotationType()}, (proxy, method, args) -> {
            switch (method.getName()) {
                case "getType":
                    return typeAnnotation.annotationType();
                case "javaTypes":
                    return typeAnnotation
                            .annotationType()
                            .getDeclaredField("JAVA_TYPES")
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
                case "protocolTypes":
                    return typeAnnotation
                            .annotationType()
                            .getDeclaredField("PROTOCOL_TYPES")
                            .get(null);
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

    ProtocolType protocolType();

    EndianPolicy[] endianPolicy();

    Class<? extends Annotation> getType();

    Type[] javaTypes();

    int size();

    ProtocolType[] protocolTypes();
}
