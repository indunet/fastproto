package org.indunet.fastproto.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.indunet.fastproto.annotation.type.*;

import java.lang.annotation.*;
import java.sql.Timestamp;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {
    int value() default -1;

    @AllArgsConstructor
    @Getter
    enum DataType {
        BINARY_TYPE(BinaryType.class, byte[].class),
        BOOLEAN_TYPE(BooleanType.class, Boolean.class),
        BYTE_TYPE(ByteType.class, Byte.class),
        DOUBLE_TYPE(DoubleType.class, Double.class),
        FLOAT_TYPE(FloatType.class, Float.class),
        INTEGER_TYPE(IntegerType.class, Integer.class),
        LONG_TYPE(LongType.class, Long.class),
        SHORT_TYPE(ShortType.class, Short.class),
        STRING_TYPE(StringType.class, String.class),
        TIMESTAMP_TYPE(TimestampType.class, Timestamp.class);

        Class<? extends Annotation> type;
        Class<?> javaType;
    }
}
