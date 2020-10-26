package org.vnet.fastproto.flow.analyse;

import org.vnet.fastproto.FastProto;
import org.vnet.fastproto.FastProtoContext;

public abstract class AbstractAnalyseFlow {
    protected AbstractAnalyseFlow next = null;

    public AbstractAnalyseFlow link(AbstractAnalyseFlow flow) {
        this.next = flow;

        return this.next;
    }

    public abstract void handle(FastProtoContext context, Object object);

    public void handleNext(FastProtoContext context, Object object) {
        if (this.next != null) {
            this.next.handle(context, object);
        }
    }
}
