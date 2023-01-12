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

 * @param offset The byte offset of the field in the binary data.
 * @param length The length of the ascii array.
 * @param endian Byte order of the character which is unicode charset, BIG or LITTLE can be set, CharArrayType use
 *               LITTLE by default.
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

    ByteOrder[] byteOrder() default {};
}
