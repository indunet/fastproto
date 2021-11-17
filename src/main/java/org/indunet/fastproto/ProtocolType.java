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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.indunet.fastproto.annotation.type.*;
import org.indunet.fastproto.exception.CodecException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
@AllArgsConstructor
@Getter
public enum ProtocolType {
    BINARY(BinaryType.class),
    BOOLEAN(BooleanType.class),
    CHARACTER(CharacterType.class),
    BYTE(ByteType.class),
    DOUBLE(DoubleType.class),
    FLOAT(FloatType.class),
    INTEGER(IntegerType.class),
    LONG(LongType.class),
    SHORT(ShortType.class),
    STRING(StringType.class),
    TIMESTAMP(TimestampType.class),
    DATE(DateType.class),
    INTEGER8(Integer8Type.class),
    INTEGER16(Integer16Type.class),
    UINTEGER8(UInteger8Type.class),
    UINTEGER16(UInteger16Type.class),
    UINTEGER32(UInteger32Type.class),
    UINTEGER64(UInteger64Type.class),
    ENUM(EnumType.class),
    LIST(ListType.class),
    ARRAY(ArrayType.class);

    protected final static String SIZE_NAME = "SIZE";
    protected final static String PROTOCOL_TYPES_NAME = "PROTOCOL_TYPES";
    protected final static String JAVA_TYPES_NAME = "JAVA_TYPES";
    Class<? extends Annotation> typeAnnotationClass;

    public static ProtocolType valueOf(@NonNull Annotation typeAnnotation) {
        return Arrays.stream(ProtocolType.values())
                .filter(t -> t.typeAnnotationClass == typeAnnotation.annotationType())
                .findFirst()
                .orElseThrow(() -> new CodecException("Cannot find matching type."));
    }

    public static boolean isSupported(@NonNull Type type) {
        return Arrays.stream(ProtocolType.values())
                .flatMap(t -> Arrays.stream(t.javaTypes()))
                .anyMatch(t -> t == type);
    }

    @SneakyThrows
    public Type[] javaTypes() {
        return (Type[]) typeAnnotationClass
                .getDeclaredField(JAVA_TYPES_NAME)
                .get(null);
    }

    public int size() {
        try {
            return this.typeAnnotationClass
                    .getDeclaredField(SIZE_NAME)
                    .getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return 0;
        }
    }

    @SneakyThrows
    public ProtocolType[] protocolTypes() {
        return (ProtocolType[]) this.typeAnnotationClass
                .getDeclaredField(PROTOCOL_TYPES_NAME)
                .get(null);
    }
}
