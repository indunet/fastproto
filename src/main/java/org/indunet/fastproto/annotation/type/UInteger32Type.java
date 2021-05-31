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
public @interface UInteger32Type {
    int SIZE = Integer.SIZE >> 3;
    int MAX_VALUE = Integer.MAX_VALUE - Integer.MIN_VALUE;
    int MIN_VALUE = 0;

    int value();
}
