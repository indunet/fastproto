package org.indunet.fastproto.flow.decode;

import lombok.val;
import org.indunet.fastproto.TypeAssist;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.DecoderFactory;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.flow.AbstractFlow;
import org.indunet.fastproto.flow.CodecContext;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Function;

public class DecodeFlow extends AbstractFlow<CodecContext> {
    public static final int FLOW_CODE = 0x0008;

    @Override
    public void process(CodecContext context) {
        val assist = context.getTypeAssist();
        val datagram = context.getDatagram();
        val protocolClass = context.getProtocolClass();
        List<DecodeContext> decodeContexts = assist.toDecodeContexts(datagram);

        decodeContexts.parallelStream()
                .forEach(c -> {
                    TypeAssist a = c.getTypeAssist();
                    Function<DecodeContext, ?> func = DecoderFactory.getDecoder(
                            a.getDecoderClass(),
                            a.getDecodeFormula());
                    try {
                        Object value = func.apply(c);
                        Object o = c.getObject();
                        a.setValue(o, value);
                    } catch (DecodeException e) {
                        throw new DecodeException(MessageFormat.format(
                                CodecError.FAIL_DECODING_FIELD.getMessage(), a.getField().toString()), e);
                    }
                });

        context.setObject(assist.getObject(protocolClass));
    }

    public int getFlowCode() {
        return FLOW_CODE;
    }
}
