package org.indunet.fastproto.annotation;

import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a struct array/collection type where each element is a POJO with FastProto annotations.
 * length() represents number of elements. Element size must be determinable at compile time for now.
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StructArrayType {
    int offset() default Integer.MIN_VALUE;

    /*
     * Reference to an offset field name in the same class. Accepts optional leading '$'.
     */
    String offsetRef() default "";
    int length() default 0; // element count; supports dynamic override via @LengthRef or lengthRef
    Class<?> element();

    String lengthRef() default "";
    boolean useSelfOnEncode() default false;
    int min() default Integer.MIN_VALUE;
    int max() default Integer.MAX_VALUE;
} 