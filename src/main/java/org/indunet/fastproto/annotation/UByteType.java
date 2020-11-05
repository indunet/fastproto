package org.indunet.fastproto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@DataType
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UByteType {
    int SIZE = Byte.SIZE >> 3;
    int MAX = Byte.MAX_VALUE - Byte.MIN_VALUE;
    int MIN = 0;

    int byteOffset();
}
