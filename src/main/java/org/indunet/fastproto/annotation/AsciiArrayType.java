package org.indunet.fastproto.annotation;

import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ascii array type which use ascii charset, each character takes 1 byte, can be used to annotate field type of Character[]/char[].

 * @param offset The byte offset of the field in the binary data.
 * @param length The length of the ascii array.
 *
 * @author Deng Ran
 * @see DataType
 * @since 3.9.1
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AsciiArrayType {
    int offset();

    int length();
}
