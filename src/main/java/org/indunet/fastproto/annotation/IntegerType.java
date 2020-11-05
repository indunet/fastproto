package org.indunet.fastproto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@DataType
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerType {
    int SIZE = Integer.SIZE >> 3;
    int MAX = Integer.MAX_VALUE;
    int MIN = Integer.MIN_VALUE;

    int byteOffset();
}
