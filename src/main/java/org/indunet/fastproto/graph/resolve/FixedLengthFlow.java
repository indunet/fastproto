package org.indunet.fastproto.graph.resolve;

import lombok.val;
import org.indunet.fastproto.annotation.FixedLength;
import org.indunet.fastproto.graph.Reference;

/**
 * @author Deng Ran
 * @since 3.1.0
 */
public class FixedLengthFlow extends ResolvePipeline {
    @Override
    public void process(Reference reference) {
        val protocolClass = reference.getProtocolClass();

        if (protocolClass.isAnnotationPresent(FixedLength.class)) {
            val enableFixedLength = protocolClass.getAnnotation(FixedLength.class);

            reference.setEnableFixedLength(enableFixedLength);
        }

        this.forward(reference);
    }
}
