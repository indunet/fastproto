package org.indunet.fastproto.graph.resolve;

import lombok.val;
import org.indunet.fastproto.annotation.EnableFixedLength;
import org.indunet.fastproto.graph.AbstractFlow;
import org.indunet.fastproto.graph.Reference;

/**
 * @author Deng Ran
 * @since 3.1.0
 */
public class EnableFixedLengthFlow extends AbstractFlow<Reference> {
    @Override
    public void process(Reference reference) {
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
