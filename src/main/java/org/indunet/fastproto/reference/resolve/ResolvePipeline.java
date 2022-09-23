package org.indunet.fastproto.reference.resolve;

import lombok.val;
import org.indunet.fastproto.reference.Reference;
import org.indunet.fastproto.pipeline.Pipeline;

/**
 * @author Deng Ran
 * @since 3.2.0
 */
public abstract class ResolvePipeline extends Pipeline<Reference> {
    @Override
    public long getCode() {
        return 0;
    }

    public static ResolvePipeline getClassPipeline() {
        val head = new EndianFlow();

        head.setNext(new EnableFixedLengthFlow())
            .setNext(new EnableVersionFlow())
            .setNext(new EnableCryptoFlow())
            .setNext(new EnableCompressFlow())
            .setNext(new EnableChecksumFlow())
            .setNext(new ConstructorFlow())
            .setNext(new CodecIgnoreFlow());

        return head;
    }

    public static ResolvePipeline getFieldPipeline() {
        val typeAnnotationFlow = new TypeAnnotationFlow();

        typeAnnotationFlow.setNext(new EndianFlow())
                .setNext(new CodecFlow())
                .setNext(new CodecIgnoreFlow());

        return typeAnnotationFlow;
    }
}
