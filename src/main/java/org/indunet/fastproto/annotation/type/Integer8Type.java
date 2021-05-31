package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @version 1.0
 * @see Type
 */
@Type
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Integer8Type {
    int SIZE = Byte.SIZE >> 3;
    int MAX_VALUE = Byte.MAX_VALUE;
    int MIN_VALUE = Byte.MIN_VALUE;

    int value();
}
