package org.indunet.fastproto.annotation;

import org.indunet.fastproto.compress.CompressPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enable compress when encode and decompress when decode.
 *
 * @author Deng Ran
 * @see CompressPolicy
 * @since 1.3.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Compress {
    CompressPolicy value() default CompressPolicy.GZIP;

    int level() default 1;
}
