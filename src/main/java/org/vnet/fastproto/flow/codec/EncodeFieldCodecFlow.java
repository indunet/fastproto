package org.vnet.fastproto.flow.codec;

import org.vnet.fastproto.CodecSession;
import org.vnet.fastproto.Endian;
import org.vnet.fastproto.FastProtoContext;
import org.vnet.fastproto.FastProtoSession;
import org.vnet.fastproto.encoder.Encoder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class EncodeFieldCodecFlow extends AbstractCodecFlow {
    protected Field field;
    protected String datagramName;
    protected Class<? extends Annotation> annotationClass;
    protected Endian endian;
    protected List<Object> args;
    protected String formulaName;

    @Override
    public void handle(FastProtoContext context, FastProtoSession session) {
        Encoder encoder = context.getCodecStrategy(this.annotationClass).getEncoder();
        encoder.encode(new CodecSession());

        this.handleNext(context, session);
    }
}
