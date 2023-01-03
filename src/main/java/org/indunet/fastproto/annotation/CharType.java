package org.indunet.fastproto.annotation;

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Char type which use unicode charset, each character takes 2 bytes, it can be used to annotate field type of Character/char.
 *
 * @param offset The byte offset of the field in the binary data.
 * @param byteOrder Byte order of the character which is unicode charset, BIG or LITTLE can be set, CharArrayType use
 *               LITTLE by default.
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

    ByteOrder[] byteOrder() default {};
}
