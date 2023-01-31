package org.indunet.fastproto.annotation;

import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Boolean array type, can be used to annotate field type of Boolean[]/boolean[]/List<Boolean>/Set<Boolean>.
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
    /*
     * The byte offset of the field in the binary data.
     */
    int byteOffset();

    /*
     * The bit offset of the field in the binary data.
     */
    int bitOffset();

    /*
     * The length of the array.
     */
    int length();

    /*
     * The bit order of the field in the binary data, its priority is higher than @DefaultBitOrder.
     */
    BitOrder[] bitOrder() default {};
}
