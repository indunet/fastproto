package org.indunet.fastproto.flow;

import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.flow.decode.DecodeFlow;
import org.indunet.fastproto.flow.decode.DecompressFlow;
import org.indunet.fastproto.flow.decode.VerifyChecksumFlow;
import org.indunet.fastproto.flow.decode.VerifyProtocolVersionFlow;
import org.indunet.fastproto.flow.encode.*;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FlowFactory {
    protected static Class<? extends AbstractFlow>[] decodeFlowClasses = new Class[] {
            DecompressFlow.class,
            VerifyChecksumFlow.class,
            VerifyProtocolVersionFlow.class,
            DecodeFlow.class};
    protected static Class<? extends AbstractFlow>[] encodeFlowClasses = new Class[] {
            InferLengthFlow.class,
            EncodeFlow.class,
            WriteProtocolVersionFlow.class,
            WriteChecksumFlow.class,
            CompressFlow.class
    };
    protected static ConcurrentMap<Integer, AbstractFlow> decodeFlows = new ConcurrentHashMap<>();
    protected static ConcurrentMap<Integer, AbstractFlow> encodeFlows = new ConcurrentHashMap<>();

    public static AbstractFlow<CodecContext> createDecode(int codecFeature) {
        return decodeFlows.computeIfAbsent(codecFeature, __ -> create(decodeFlowClasses, codecFeature));
    }

    public static AbstractFlow<CodecContext> createEncode(int codecFeature) {
        return encodeFlows.computeIfAbsent(codecFeature, __ -> create(encodeFlowClasses, codecFeature));
    }

    protected static AbstractFlow create(Class<? extends AbstractFlow>[] flowClasses, int codecFeature) {
        AbstractFlow[] array = Arrays.stream(flowClasses)
                .map(c -> {
                    try {
                        return (AbstractFlow) c.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new DecodeException(CodecError.FAIL_CREATING_DECODE_FLOW, e);
                    }
                })
                .filter(f -> (f.getFlowCode() & codecFeature) == 0)
                .toArray(AbstractFlow[]::new);

        AbstractFlow flow = array[0];

        for (int i = 1; i < array.length; i ++) {
            flow.setNext(array[i]);
            flow = flow.next;
        }

        return array[0];
    }
}
