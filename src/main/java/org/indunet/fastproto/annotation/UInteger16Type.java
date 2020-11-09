package org.indunet.fastproto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@DataType
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UInteger16Type {
    int SIZE = Short.SIZE >> 3;
    int MAX = Short.MAX_VALUE - Short.MIN_VALUE;
    int MIN = 0;

    int byteOffset();
}
