package org.indunet.fastproto.annotation;

import org.indunet.fastproto.EndianPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify endian according to hardware platform.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Endian {
    EndianPolicy value() default EndianPolicy.LITTLE;
}
