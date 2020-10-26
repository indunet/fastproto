package org.vnet.fastproto.flow.analyse;

import org.vnet.fastproto.FastProtoContext;

public class AnalyseAfterDecodeFlow extends AbstractAnalyseFlow {
    @Override
    public void handle(FastProtoContext context, Object object) {
        this.next.handleNext(context, object);
    }
}
