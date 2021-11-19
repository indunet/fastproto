package org.indunet.fastproto.graph.resolve;

import lombok.val;
import org.indunet.fastproto.annotation.EnableFixedLength;
import org.indunet.fastproto.graph.AbstractFlow;
import org.indunet.fastproto.graph.Reference;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

/**
 * @author Deng Ran
 * @since 3.1.0
 */
@Rule(name = "length")
public class EnableFixedLengthFlow extends AbstractFlow<Reference> {
    @Condition
    public boolean evaluate(@Fact("reference") Reference reference) {
        return reference.getProtocolClass().isAnnotationPresent(EnableFixedLength.class);
    }

    @Action
    @Override
    public void process(@Fact("reference") Reference reference) {
        val protocolClass = reference.getProtocolClass();

        if (protocolClass.isAnnotationPresent(EnableFixedLength.class)) {
            val enableFixedLength = protocolClass.getAnnotation(EnableFixedLength.class);

            reference.setEnableFixedLength(enableFixedLength);
        }

        this.nextFlow(reference);
    }

    @Override
    public long getFlowCode() {
        return 0;
    }
}
