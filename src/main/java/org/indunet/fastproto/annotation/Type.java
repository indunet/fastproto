package org.indunet.fastproto.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.indunet.fastproto.annotation.type.*;

import java.lang.annotation.*;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {
    @AllArgsConstructor
    @Getter
    enum DataType {
        BINARY_TYPE(BinaryType.class),
        BOOLEAN_TYPE(BooleanType.class),
        BYTE_TYPE(ByteType.class),
        DOUBLE_TYPE(DoubleType.class),
        FLOAT_TYPE(FloatType.class),
        INTEGER_TYPE(IntegerType.class),
        LONG_TYPE(LongType.class),
        SHORT_TYPE(ShortType.class),
        STRING_TYPE(StringType.class),
        TIMESTAMP_TYPE(TimestampType.class);

        Class<? extends Annotation> type;
    }
}
