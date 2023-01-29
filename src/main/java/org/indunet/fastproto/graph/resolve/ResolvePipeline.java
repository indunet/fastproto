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
        val head = new ByteOrderFlow();

        head.setNext(new ConstructorFlow())
            .setNext(new CodecIgnoreFlow());

        return head;
    }

    public static ResolvePipeline getFieldPipeline() {
        val typeAnnotationFlow = new TypeAnnotationFlow();

        typeAnnotationFlow.setNext(new ByteOrderFlow())
                .setNext(new BitOrderFlow())
                .setNext(new FormulaFlow())
                .setNext(new CodecFlow())
                .setNext(new CodecIgnoreFlow());

        return typeAnnotationFlow;
    }
}
