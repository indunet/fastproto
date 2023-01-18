package org.indunet.fastproto.annotation;

import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Boolean array type, can be used to annotate field type of Boolean[]/boolean[].
 *
 * @param byteOffset The byte offset of the field in the binary data.
 * @param bitOffset The bit offset of the field in the binary data.
 * @param length The length of the boolean array.
 * @param mode It is used to distinguish between the different bits in the byte and to determine their relative
 *             significance or weight. BitOrder.LSB_0 means BIT_0 is LSB while BitOrder.MSB_0 means BIT_0 is MSB,
 *             BoolArrayType uses LSB_0 by default.
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
    int byteOffset();

    int bitOffset();

    int length();

    BitOrder[] bitOrder() default {};
}
