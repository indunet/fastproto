package org.vnet.fastproto.flow.analyse;

import org.vnet.fastproto.FastProtoContext;
import org.vnet.fastproto.annotation.BeforeDecode;
import org.vnet.fastproto.annotation.BeforeEncode;
import org.vnet.fastproto.util.ReflectUtils;

import java.lang.reflect.Method;

public class AnalyseBeforeDecodeFlow extends AbstractAnalyseFlow {
    @Override
    public void handle(FastProtoContext context, Object object) {
        this.decodeFlow(context, object);
        this.encodeFlow(context, object);
    }

    protected void decodeFlow(FastProtoContext context, Object object) {

    }

    protected void encodeFlow(FastProtoContext context, Object object) {
        Method[] beforeDecodeMethods = ReflectUtils.getMethods(BeforeDecode.class);
        Method[] beforeEncodeMethods = ReflectUtils.getMethods(BeforeEncode.class);
    }
}
