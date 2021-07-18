package org.indunet.fastproto.pipeline.validate;

import lombok.val;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.DecodeFormulaException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.ValidationContext;
import org.indunet.fastproto.util.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class FieldFlow extends AbstractFlow<ValidationContext> {
    public static int FIELD_FLOW = 0x01;

    @Override
    public void process(ValidationContext context) {
        val typeAnnotation = context.getTypeAnnotation();
        val field = context.getField();
        Class<? extends Function> decodeFormula;
        Class<? extends Function> encodeFormula;


        try {
            decodeFormula = TypeUtils.decodeFormula(typeAnnotation);
            encodeFormula = TypeUtils.encodeFormula(typeAnnotation);
        }  catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new DecodeFormulaException(
                    MessageFormat.format(
                            CodecError.FAIL_GETTING_DECODE_FORMULA.getMessage(),
                            typeAnnotation.annotationType().getName(), field.getName()), e);
        }

        if (decodeFormula == null && encodeFormula == null) {
            val f = typeAnnotationClass.getField("JAVA_TYPES");

            Arrays.stream((Type[]) f.get(typeAnnotation))
                    .filter(t -> t == field.getType()
                            || (field.getType().isEnum() && (((Class<?>) t).isAssignableFrom(field.getType()))))
                    // Enum type.
                    .findAny()
                    .orElseThrow(() -> new CodecException(MessageFormat.format(
                            CodecError.ANNOTATION_FIELD_NOT_MATCH.getMessage(), typeAnnotation.annotationType().getName(), field.getName())));
        }
    }

    @Override
    public long getFlowCode() {
        return FIELD_FLOW;
    }
}
