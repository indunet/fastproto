package org.indunet.fastproto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@DataType
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleType {
    int SIZE = Double.SIZE >> 3;
    double MAX = Double.MAX_VALUE;
    double MIN = Double.MIN_VALUE;

    int byteOffset();
}
