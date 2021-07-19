package org.indunet.fastproto.pipeline.validate;

import lombok.val;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeFormulaException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.ValidationContext;
import org.indunet.fastproto.util.TypeUtils;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Decode formula validation flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class DecodeFormulaFlow extends AbstractFlow<ValidationContext> {
    public static final long DECODE_FORMULA_FLOW = 0x02;

    @Override
    public void process(ValidationContext context) {
        val decodeFormula = context.getDecodeFormula();
        val typeAnnotation = context.getTypeAnnotation();
        val field = context.getField();

        if (decodeFormula != null) {
            Arrays.stream(decodeFormula.getGenericInterfaces())
                    .filter(i -> i instanceof ParameterizedType)
                    .map(i -> ((ParameterizedType) i).getActualTypeArguments())
                    .map(a -> a[1])
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
                    .orElseThrow(() -> new DecodeFormulaException(MessageFormat.format(
                            CodecError.ANNOTATION_FIELD_NOT_MATCH.getMessage(), typeAnnotation.annotationType().getName(), field.getName())));
        }

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return DECODE_FORMULA_FLOW;
    }
}
