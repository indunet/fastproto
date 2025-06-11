package org.indunet.fastproto.annotation;

import java.lang.annotation.*;

/**
 * Mark protocol class for codec generation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface GenerateCodec {
}
