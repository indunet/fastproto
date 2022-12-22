package org.indunet.fastproto.annotation;

import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Boolean array type, corresponding to Java Boolean[]/boolean[].
 *
 * @author Deng Ran
 * @see DataType
 * @since 3.8.3
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BoolArrayType {
    int MSB_0 = 0x01;
    int LSB_0 = 0x02;

    int byteOffset();

    int bitOffset();

    int length();

    int mode() default MSB_0;
}
