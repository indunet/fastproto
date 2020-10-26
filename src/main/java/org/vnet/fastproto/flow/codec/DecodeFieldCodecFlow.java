package org.vnet.fastproto.flow.codec;

import org.vnet.fastproto.CodecSession;
import org.vnet.fastproto.Endian;
import org.vnet.fastproto.FastProtoContext;
import org.vnet.fastproto.FastProtoSession;
import org.vnet.fastproto.decoder.Decoder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class DecodeFieldCodecFlow extends AbstractCodecFlow {
    protected Field field;
    protected String datagramName;
    protected Class<? extends Annotation> annotationClass;
    protected Endian endian;
    protected List<Object> args;
    protected String formulaName;


    @Override
    public void handle(FastProtoContext context, FastProtoSession session) {
        Decoder decoder = context.getCodecStrategy(this.annotationClass).getDecoder();
        decoder.decode(new CodecSession());

        this.handleNext(context, session);
    }
}
