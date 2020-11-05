package org.indunet.fastproto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@DataType
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LongType {
    int SIZE = Long.SIZE >> 3;
    long MAX = Long.MAX_VALUE;
    long MIN = Long.MIN_VALUE;

    int byteOffset();
}
