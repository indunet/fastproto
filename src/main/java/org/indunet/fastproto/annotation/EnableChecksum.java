package org.indunet.fastproto.annotation;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.checksum.CheckPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @since 1.6.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableChecksum {
    int value() default -2;

    int start() default 0;

    int length() default -3;

    CheckPolicy checkPolicy() default CheckPolicy.CRC16;

    int poly() default 0;

    EndianPolicy[] endianPolicy() default {};
}
