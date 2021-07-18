package org.indunet.fastproto.pipeline.validate;

import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.ValidationContext;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class TimestampFlow implements AbstractFlow<ValidationContext> {
    @Override
    public void process(ValidationContext context) {

    }

    @Override
    public long getFlowCode() {
        return 0;
    }
}
