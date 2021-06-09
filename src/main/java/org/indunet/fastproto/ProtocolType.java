package org.indunet.fastproto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.indunet.fastproto.annotation.type.*;
import org.indunet.fastproto.exception.CodecException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
@AllArgsConstructor
@Getter
public enum ProtocolType {
    BINARY(BinaryType.class, true),
    BOOLEAN(BooleanType.class, true),
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
    UINTEGER32(UInteger32Type.class, false);

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
}
