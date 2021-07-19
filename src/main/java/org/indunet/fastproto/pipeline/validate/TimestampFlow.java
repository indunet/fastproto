package org.indunet.fastproto.pipeline.validate;

import lombok.val;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.TimestampType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.ValidationContext;

import java.util.concurrent.TimeUnit;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class TimestampFlow extends AbstractFlow<ValidationContext> {
    public static final long TIMESTAMP_FLOW = 0x20;

    @Override
    public void process(ValidationContext context) {
        val typeAnnotation = context.getTypeAnnotation();

        if (typeAnnotation instanceof TimestampType) {
            val protocolType = ((TimestampType) typeAnnotation).protocolType();
            val unit = ((TimestampType) typeAnnotation).unit();

            val condition1 = protocolType == ProtocolType.UINTEGER32 && unit == TimeUnit.SECONDS;
            val condition2 = protocolType == ProtocolType.LONG && unit == TimeUnit.MILLISECONDS;

            if (!condition1 && !condition2) {
                throw new DecodeException(CodecError.ILLEGAL_TIMESTAMP_PARAMETERS);
            }
        }

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return TIMESTAMP_FLOW;
    }
}
