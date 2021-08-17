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
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CodecException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
@AllArgsConstructor
@Getter
public enum ProtocolType {
    // Byte array type.
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
    // java.sql.Timestamp
    TIMESTAMP(TimestampType.class),
    // java.util.Date
    DATE(DateType.class),
    // int8
    INTEGER8(Integer8Type.class),
    // int16
    INTEGER16(Integer16Type.class),
    // uint8
    UINTEGER8(UInteger8Type.class),
    // uint16
    UINTEGER16(UInteger16Type.class),
    // uint32
    UINTEGER32(UInteger32Type.class),
    // uint64
    UINTEGER64(UInteger64Type.class),
    ENUM(EnumType.class),
    // List of primitive type.
    LIST(ListType.class),
    // Array of primitive type.
    ARRAY(ArrayType.class);

    Class<? extends Annotation> typeAnnotationClass;

    protected final static String SIZE_NAME = "SIZE";
    protected final static String PROTOCOL_TYPES_NAME = "PROTOCOL_TYPES";
    protected final static String JAVA_TYPES_NAME = "JAVA_TYPES";
    protected final static String AUTO_TYPE_NAME = "AUTO_TYPE";

    protected final static String BYTE_OFFSET_NAME = "value";
    protected final static String BIT_OFFSET_NAME = "bitOffset";
    protected final static String LENGTH_NAME = "length";
    protected final static String ENCODE_FORMULA_NAME = "beforeEncode";
    protected final static String DECODE_FORMULA_NAME = "afterDecode";

    @SneakyThrows
    public boolean autoType() {
        val field = typeAnnotationClass.getField(AUTO_TYPE_NAME);

        return field.getBoolean(null);
    }

    public Type javaType() {
        return javaTypes()[0];
    }

    @SneakyThrows
    public Type[] javaTypes() {
        val field = typeAnnotationClass.getField(JAVA_TYPES_NAME);

        return (Type[]) field.get(null);
    }

    public boolean match(Type type) {
        return Arrays.stream(this.javaTypes())
                .anyMatch(t -> t == type);
    }

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

    @SneakyThrows
    public static Type[] supportedTypes() {
        return Arrays.stream(ProtocolType.values())
                .map(ProtocolType::getTypeAnnotationClass)
                .flatMap(c -> Arrays.stream(ProtocolType.javaTypes(c)))
                .toArray(Type[]::new);
    }

    public static Type wrapperClass(@NonNull String name) {
        switch (name) {
            case "boolean":
                return Boolean.class;
            case "byte":
                return Byte.class;
            case "char":
                return Character.class;
            case "short":
                return Short.class;
            case "int":
                return Integer.class;
            case "long":
                return Long.class;
            case "float":
                return Float.class;
            case "double":
                return Double.class;
            default:
                throw new CodecException(
                        MessageFormat.format(CodecError.UNSUPPORTED_TYPE.getMessage(), name));
        }
    }

    @SneakyThrows
    public static int size(@NonNull ProtocolType type) {
        return type.getTypeAnnotationClass()
                .getDeclaredField(SIZE_NAME)
                .getInt(null);
    }

    @SneakyThrows
    public static int size(@NonNull Class<? extends Annotation> typeAnnotationClass) {
        return typeAnnotationClass
                .getDeclaredField(SIZE_NAME)
                .getInt(null);
    }

    @SneakyThrows
    public static ProtocolType[] protocolTypes(@NonNull Annotation typeAnnotation) {
        return (ProtocolType[]) typeAnnotation.annotationType()
                .getDeclaredField(PROTOCOL_TYPES_NAME)
                .get(typeAnnotation);
    }

    public static Class<? extends Function> encodeFormula(@NonNull Annotation typeAnnotation) {
        return formula(typeAnnotation, ENCODE_FORMULA_NAME);
    }

    public static Class<? extends Function> decodeFormula(@NonNull Annotation typeAnnotation) {
        return formula(typeAnnotation, DECODE_FORMULA_NAME);
    }

    @SneakyThrows
    protected static Class<? extends Function> formula(@NonNull Annotation typeAnnotation, @NonNull String name) {
        val method = typeAnnotation.getClass().getMethod(name);
        val array = method.invoke(typeAnnotation);

        return Optional.of(array)
                .filter(a -> a.getClass().isArray())
                .filter(a -> Array.getLength(a) >= 1)
                .map(a -> Array.get(a, 0))
                .map(o -> (Class<? extends Function>) o)
                .orElse(null);
    }

    @SneakyThrows
    public static Type[] javaTypes(@NonNull Annotation typeAnnotation) {
        return (Type[]) typeAnnotation
                .getClass()
                .getField(JAVA_TYPES_NAME)
                .get(null);
    }

    @SneakyThrows
    public static Type[] javaTypes(@NonNull Class<? extends Annotation> typeAnnotationClass) {
        return (Type[]) typeAnnotationClass
                .getDeclaredField(JAVA_TYPES_NAME)
                .get(null);
    }

    public static int byteOffset(@NonNull Annotation typeAnnotation) {
        try {
            return (Integer) typeAnnotation
                    .getClass()
                    .getMethod(BYTE_OFFSET_NAME)
                    .invoke(typeAnnotation);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return 0;
        }
    }

    public static int bitOffset(@NonNull Annotation typeAnnotation) {
        try {
            return (Integer) typeAnnotation
                    .getClass()
                    .getMethod(BIT_OFFSET_NAME)
                    .invoke(typeAnnotation);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return 0;
        }
    }

    public static int size(@NonNull Annotation typeAnnotation) {
        try {
            return typeAnnotation
                    .getClass()
                    .getField(SIZE_NAME)
                    .getInt(typeAnnotation);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return 0;
        }
    }

    public static int length(@NonNull Annotation typeAnnotation) {
        try {
            return (Integer) typeAnnotation
                    .getClass()
                    .getMethod(LENGTH_NAME)
                    .invoke(typeAnnotation);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return 0;
        }
    }
}
