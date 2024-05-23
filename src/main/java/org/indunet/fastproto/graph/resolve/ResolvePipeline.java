package org.indunet.fastproto.graph.resolve;

import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.pipeline.Pipeline;

/**
 * ResolvePipeline Class.
 * This class is responsible for resolving the reference in the context.
 * It checks the reference for the necessary information and sets the corresponding properties in the reference accordingly.
 * This class extends the Pipeline class and overrides the process method to implement its functionality.
 *
 * @author Deng Ran
 * @since 3.2.0
 */
public abstract class ResolvePipeline extends Pipeline<Reference> {
    @Override
    public long getCode() {
        return 0;
    }

    public static ResolvePipeline getClassPipeline() {
        return buildPipeline(new ByteOrderFlow(), new ConstructorFlow(), new CodecIgnoreFlow());
    }

    public static ResolvePipeline getFieldPipeline() {
        return buildPipeline(new TypeAnnotationFlow(), new ByteOrderFlow(), new BitOrderFlow(), new FormulaFlow(), new CodecFlow(), new CodecIgnoreFlow());
    }

    private static ResolvePipeline buildPipeline(ResolvePipeline... pipelines) {
        for (int i = 0; i < pipelines.length - 1; i++) {
            pipelines[i].setNext(pipelines[i + 1]);
        }
        return pipelines[0];
    }
}
