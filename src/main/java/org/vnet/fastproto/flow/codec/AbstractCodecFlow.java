package org.vnet.fastproto.flow.codec;

import org.vnet.fastproto.FastProtoContext;
import org.vnet.fastproto.FastProtoSession;

public abstract class AbstractCodecFlow {
    protected AbstractCodecFlow next = null;

    public AbstractCodecFlow link(AbstractCodecFlow flow) {
        this.next = flow;

        return this.next;
    }

    public abstract void handle(FastProtoContext context, FastProtoSession session);

    public void handleNext(FastProtoContext context, FastProtoSession session) {
        if (this.next != null) {
            this.next.handle(context, session);
        }
    }
}
