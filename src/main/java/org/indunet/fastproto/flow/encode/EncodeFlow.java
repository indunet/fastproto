package org.indunet.fastproto.flow.encode;

import lombok.val;
import org.indunet.fastproto.TypeAssist;
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.encoder.EncoderFactory;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.flow.AbstractFlow;
import org.indunet.fastproto.flow.CodecContext;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Consumer;

public class EncodeFlow extends AbstractFlow<CodecContext> {
    public static final int FLOW_CODE = 0x0200;

    @Override
    public void process(CodecContext context) {
        val assist = context.getTypeAssist();
        val object = context.getObject();
        val datagram = context.getDatagram();

        List<EncodeContext> encodeContexts = assist.toEncodeContexts(object, datagram);

        encodeContexts.stream()
                .forEach(c -> {
                    TypeAssist a = c.getTypeAssist();

                    try {
                        if (a.getEncodeFormula() != null) {
                            Object o = EncoderFactory.getFormula(c.getTypeAssist().getEncodeFormula())
                                    .apply(c.getValue());
                            c.setValue(o);
                        }

                        Consumer<EncodeContext> consumer = EncoderFactory.getEncoder(c.getTypeAssist().getEncoderClass());
                        consumer.accept(c);
                    } catch (EncodeException e) {
                        throw new EncodeException(MessageFormat.format(
                                CodecError.FAIL_ENCODING_FIELD.getMessage(), a.getField().toString()),
                                e);
                    }
                });

        this.nextFlow(context);
    }

    @Override
    public int getFlowCode() {
        return FLOW_CODE;
    }
}
