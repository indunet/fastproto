package org.indunet.fastproto.annotation;

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Character array type which use unicode charset, each character takes 2 byte, can be used to annotate field type of Character[]/char[].

 * @author Deng Ran
 * @see DataType
 * @since 3.9.1
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CharArrayType {
    /*
     * The byte offset of the field in the binary data.
     */
    int offset();

    /*
     * The length of the array.
     */
    int length();

    /*
     * The byte order of the field in the binary data, its priority is higher than @DefaultByteOrder.
     */
    ByteOrder[] byteOrder() default {};
}
