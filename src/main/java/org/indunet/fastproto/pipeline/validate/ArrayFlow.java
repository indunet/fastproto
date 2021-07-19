package org.indunet.fastproto.pipeline.validate;

import lombok.val;
import org.indunet.fastproto.annotation.type.ArrayType;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.ValidationContext;
import org.indunet.fastproto.util.TypeUtils;

import java.util.Arrays;

/**
 * Array type validation flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class ArrayFlow extends AbstractFlow<ValidationContext> {
    public static final long ARRAY_FLOW = 0x10;

    @Override
    public void process(ValidationContext context) {
        val typeAnnotation = context.getTypeAnnotation();

        if (typeAnnotation instanceof ArrayType) {
            val protocolType = ((ArrayType) typeAnnotation).protocolType();
            val protocolTypes = TypeUtils.protocolTypes(typeAnnotation);

            Arrays.stream(protocolTypes)
                    .filter(t -> t == protocolType)
                    .findAny()
                    .orElseThrow(CodecException::new);
        }

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return ARRAY_FLOW;
    }
}
