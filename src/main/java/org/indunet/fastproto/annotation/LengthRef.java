package org.indunet.fastproto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that the annotated field's effective length should be resolved dynamically.
 * - value: the source field name providing the length (Number type)
 * - useSelfOnEncode: when true, encoder uses actual runtime length of this field's value
 * - min/max: optional constraints to validate effective length in both decode and encode
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LengthRef {
    String value();
    boolean useSelfOnEncode() default false;
    int min() default Integer.MIN_VALUE;
    int max() default Integer.MAX_VALUE;
} 