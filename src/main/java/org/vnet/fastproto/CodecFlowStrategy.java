package org.vnet.fastproto;

import org.vnet.fastproto.flow.codec.AbstractCodecFlow;

public class CodecFlowStrategy {
    protected Class clazz;
    protected AbstractCodecFlow encodeFlow;
    protected AbstractCodecFlow decodeFlow;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public AbstractCodecFlow getEncodeFlow() {
        return encodeFlow;
    }

    public void setEncodeFlow(AbstractCodecFlow encodeFlow) {
        this.encodeFlow = encodeFlow;
    }

    public AbstractCodecFlow getDecodeFlow() {
        return decodeFlow;
    }

    public void setDecodeFlow(AbstractCodecFlow decodeFlow) {
        this.decodeFlow = decodeFlow;
    }
}
