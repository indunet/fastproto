package org.indunet.fastproto.pipeline.checksum;

import org.indunet.fastproto.checksum.*;
import org.indunet.fastproto.graph.Graph;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.pipeline.FlowCode;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.PipelineContext;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Pipeline flow that calculates CRC checksum after encoding.
 */
public class ChecksumEncodeFlow extends Pipeline<PipelineContext> {
    @Override
    public void process(PipelineContext context) {
        Graph graph = context.getGraph();
        byte[] bytes = context.getOutputStream().toByteBuffer().toBytes();

        for (Reference ref : graph.getValidReferences()) {
            Field field = ref.getField();
            if (field.isAnnotationPresent(org.indunet.fastproto.annotation.Checksum.class)) {
                org.indunet.fastproto.annotation.Checksum cs =
                        field.getAnnotation(org.indunet.fastproto.annotation.Checksum.class);
                int start = cs.start();
                int len = cs.length() > 0 ? cs.length() : bytes.length - start;
                byte[] segment = Arrays.copyOfRange(bytes, start, Math.min(start + len, bytes.length));
                int value = compute(cs.type(), segment);
                ref.setValue(cast(value, field.getType()));
                ref.encoder(context.getOutputStream());
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

    private static Object cast(int value, Class<?> type) {
        if (type == byte.class || type == Byte.class) {
            return (byte) value;
        } else if (type == short.class || type == Short.class) {
            return (short) value;
        } else if (type == long.class || type == Long.class) {
            return (long) value;
        } else {
            return value;
        }
    }

    @Override
    public long getCode() {
        return FlowCode.ENCODE_FLOW_CODE;
    }
}
