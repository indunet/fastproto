package org.indunet.fastproto.annotation;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Char array type, corresponding to Java Integer/int.
 *
 * @author Deng Ran
 * @see DataType
 * @since 3.9.1
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CharArrayType {
    int offset();

    int length();

    EndianPolicy[] endian() default {};
}
