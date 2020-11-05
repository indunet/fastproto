package org.indunet.fastproto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@DataType
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UIntegerType {
    int SIZE = Integer.SIZE >> 3;
    long MAX = Integer.MAX_VALUE - Integer.MIN_VALUE;
    long MIN = 0L;

    int byteOffset();
}
