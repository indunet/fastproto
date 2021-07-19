package org.indunet.fastproto.pipeline.validate;

import lombok.val;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeFormulaException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.ValidationContext;
import org.indunet.fastproto.util.TypeUtils;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Encode formula validation flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class EncodeFormulaFlow extends AbstractFlow<ValidationContext> {
    public static final long ENCODE_FORMULA_FLOW = 0x04;

    @Override
    public void process(ValidationContext context) {
        val encodeFormula = context.getEncodeFormula();
        val typeAnnotation = context.getTypeAnnotation();
        val field = context.getField();

        // Validate encoder parameter type.
        if (encodeFormula != null) {
            Arrays.stream(encodeFormula.getGenericInterfaces())
                    .filter(i -> i instanceof ParameterizedType)
                    .map(i -> ((ParameterizedType) i).getActualTypeArguments())
                    .map(a -> a[0])
                    .filter(t -> {
                        if (field.getType().isPrimitive()) {
                            return t == TypeUtils.wrapperClass(field.getType().getName());
                        } else if (field.getType().isEnum()) {
                            // Enum type.
                            return ((Class<?>) t).isAssignableFrom(field.getType());
                        } else {
                            return t == field.getType();
                        }
                    }).findAny()
                    .orElseThrow(() -> new EncodeFormulaException(MessageFormat.format(
                            CodecError.ANNOTATION_FIELD_NOT_MATCH.getMessage(), typeAnnotation.annotationType().getName(), field.getName())));
        }

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return ENCODE_FORMULA_FLOW;
    }
}
