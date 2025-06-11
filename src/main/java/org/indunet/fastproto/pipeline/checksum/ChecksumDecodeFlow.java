package org.indunet.fastproto.pipeline.checksum;

import org.indunet.fastproto.checksum.*;
import org.indunet.fastproto.graph.Graph;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.pipeline.FlowCode;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.PipelineContext;
import org.indunet.fastproto.exception.DecodingException;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Pipeline flow that verifies CRC checksum after decoding.
 */
public class ChecksumDecodeFlow extends Pipeline<PipelineContext> {
    @Override
    public void process(PipelineContext context) {
        Graph graph = context.getGraph();
        byte[] bytes = context.getInputStream().toByteBuffer().toBytes();

        for (Reference ref : graph.getValidReferences()) {
            Field field = ref.getField();
            if (field.isAnnotationPresent(org.indunet.fastproto.annotation.Checksum.class)) {
                org.indunet.fastproto.annotation.Checksum cs =
                        field.getAnnotation(org.indunet.fastproto.annotation.Checksum.class);
                int start = cs.start();
                int len = cs.length() > 0 ? cs.length() : bytes.length - start;
                byte[] segment = Arrays.copyOfRange(bytes, start, Math.min(start + len, bytes.length));
                int expected = compute(cs.type(), segment);
                int actual = ((Number) ref.getValue().get()).intValue();
                if (expected != actual) {
                    throw new DecodingException("Checksum mismatch on field " + field.getName());
                }
            }
        }

        this.forward(context);
    }

    private static int compute(CRCType type, byte[] data) {
        switch (type) {
            case CRC8:
                return new CRC8().calculate(data);
            case CRC16:
                return new CRC16().calculate(data);
            case CRC32:
                return new CRC32().calculate(data);
            default:
                return 0;
        }
    }

    @Override
    public long getCode() {
        return FlowCode.DECODE_FLOW_CODE;
    }
}
