package org.vnet.fastproto.flow.codec;

import org.vnet.fastproto.FastProtoContext;
import org.vnet.fastproto.FastProtoSession;

import java.lang.reflect.Method;
import java.util.List;

public class InvokeMethodCodecFlow extends AbstractCodecFlow {
    protected Method method;
    protected List<Object> args;

    @Override
    public void handle(FastProtoContext context, FastProtoSession session) {
        this.handleNext(context, session);
    }
}
