package org.indunet.fastproto.annotation;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Char type which takes 2 byte, corresponding to Java Character/char.
 *
 * @author Deng Ran
 * @see DataType
 * @since 3.8.4
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CharType {
    int SIZE = Character.SIZE >>> 3;

    int offset();

    EndianPolicy[] endian() default {};
}
