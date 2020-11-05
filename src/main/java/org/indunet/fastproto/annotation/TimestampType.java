package org.indunet.fastproto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@DataType
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimestampType {
    int byteOffset();
    int length() default Long.SIZE >> 3;
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
