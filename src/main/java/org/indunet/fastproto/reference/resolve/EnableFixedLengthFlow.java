package org.indunet.fastproto.reference.resolve;

import lombok.val;
import org.indunet.fastproto.annotation.EnableFixedLength;
import org.indunet.fastproto.reference.Reference;

/**
 * @author Deng Ran
 * @since 3.1.0
 */
public class EnableFixedLengthFlow extends ResolvePipeline {
    @Override
    public void process(Reference reference) {
        val protocolClass = reference.getProtocolClass();

        if (protocolClass.isAnnotationPresent(EnableFixedLength.class)) {
            val enableFixedLength = protocolClass.getAnnotation(EnableFixedLength.class);

            reference.setEnableFixedLength(enableFixedLength);
        }

        this.forward(reference);
    }
}
