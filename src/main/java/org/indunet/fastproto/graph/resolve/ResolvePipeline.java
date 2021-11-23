package org.indunet.fastproto.graph.resolve;

import lombok.val;
import org.indunet.fastproto.graph.Reference;
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
        val endianFlow = new EndianFlow();
        val enableFixedLength = new EnableFixedLengthFlow();
        val enableProtocolVersionFlow = new EnableProtocolVersionFlow();
        val enableCryptoFlow = new EnableCryptoFlow();
        val enableCompressFlow = new EnableCompressFlow();
        val enableChecksumFlow = new EnableChecksumFlow();
        val constructorFlow = new ConstructorFlow();
        val codecIgnoreFlow = new CodecIgnoreFlow();

        endianFlow
                .setNext(enableFixedLength)
                .setNext(enableProtocolVersionFlow)
                .setNext(enableCryptoFlow)
                .setNext(enableCompressFlow)
                .setNext(enableChecksumFlow)
                .setNext(constructorFlow)
                .setNext(codecIgnoreFlow);

        return endianFlow;
    }

    public static ResolvePipeline getFieldPipeline() {
        val typeAnnotationFlow = new TypeAnnotationFlow();
        val endianFlow = new EndianFlow();
        val codecFlow = new CodecFlow();
        val codecIgnoreFlow = new CodecIgnoreFlow();

        typeAnnotationFlow.setNext(endianFlow)
                .setNext(codecFlow)
                .setNext(codecIgnoreFlow);

        return typeAnnotationFlow;
    }
}
