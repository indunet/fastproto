package org.vnet.fastproto.flow;

import org.vnet.fastproto.FastProtoContext;
import org.vnet.fastproto.flow.analyse.*;
import org.vnet.fastproto.flow.codec.AbstractCodecFlow;

public class FlowFactory {
    public static AbstractAnalyseFlow createAnalyseFlow() {
        return new AnalyseBeforeDecodeFlow()
                .link(new AnalyseFieldFlow())
                .link(new AnalyseAfterDecodeFlow());
    }
}
