package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @see Type
 * @since 2.0.0
 */
@Type
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Integer16Type {
    int SIZE = Short.SIZE >> 3;
    int MAX_VALUE = Short.MAX_VALUE;
    int MIN_VALUE = Short.MIN_VALUE;

    int value();
}
