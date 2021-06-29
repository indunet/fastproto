package org.indunet.fastproto.flow.decode;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.VersionAssist;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.ProtocolVersionException;
import org.indunet.fastproto.flow.AbstractFlow;
import org.indunet.fastproto.flow.CodecContext;

public class VerifyProtocolVersionFlow extends AbstractFlow<CodecContext> {
    public static final int FLOW_CODE = 0x0004;

    @Override
    public void process(@NonNull CodecContext context) {
        val datagram = context.getDatagram();
        val protocolClass = context.getProtocolClass();

        if (!VersionAssist.validate(datagram, protocolClass)) {
            throw new ProtocolVersionException(CodecError.PROTOCOL_VERSION_NOT_MATCH);
        } else {
            this.nextFlow(context);
        }
    }

    @Override
    public int getFlowCode() {
        return FLOW_CODE;
    }
}
