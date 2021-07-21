/*
 * Copyright 2019-2021 indunet
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

import lombok.*;
import org.indunet.fastproto.annotation.type.*;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.util.TypeUtils;

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
    BINARY(BinaryType.class, true),
    BOOLEAN(BooleanType.class, true),
    CHARACTER(CharacterType.class, true),
    BYTE(ByteType.class, true),
    DOUBLE(DoubleType.class, true),
    FLOAT(FloatType.class, true),
    INTEGER(IntegerType.class, true),
    LONG(LongType.class, true),
    SHORT(ShortType.class, true),
    STRING(StringType.class, true),
    TIMESTAMP(TimestampType.class, true),
    INTEGER8(Integer8Type.class, false),
    INTEGER16(Integer16Type.class, false),
    UINTEGER8(UInteger8Type.class, false),
    UINTEGER16(UInteger16Type.class, false),
    UINTEGER32(UInteger32Type.class, false),
    UINTEGER64(UInteger64Type.class, true),
    ENUM(EnumType.class, true);

    Class<? extends Annotation> typeAnnotationClass;
    Boolean autoType;

    public static ProtocolType valueOf(Class<? extends Annotation> clazz) {
        return Arrays.stream(ProtocolType.values())
                .filter(t -> t.typeAnnotationClass == clazz)
                .findFirst()
                .orElseThrow(CodecException::new);
    }

    public static ProtocolType byAutoType(Type type) {
        return Arrays.stream(ProtocolType.values())
                .filter(t -> Arrays.asList(t.javaTypes()).contains(type))
                .findFirst()
                .orElseThrow(CodecException::new);
    }

    public Type javaType() {
        return javaTypes()[0];
    }

    @SneakyThrows
    public Type[] javaTypes() {
        val field = typeAnnotationClass.getField("JAVA_TYPES");

        return (Type[]) field.get(null);
    }

    @SneakyThrows
    public static Type[] supportedTypes() {
        return Arrays.stream(ProtocolType.values())
                .map(ProtocolType::getTypeAnnotationClass)
                .flatMap(c -> Arrays.stream(TypeUtils.javaTypes(c)))
                .toArray(Type[]::new);
    }
}
