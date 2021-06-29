package org.indunet.fastproto.flow.encode;

import lombok.val;
import org.indunet.fastproto.VersionAssist;
import org.indunet.fastproto.flow.AbstractFlow;
import org.indunet.fastproto.flow.CodecContext;

public class WriteProtocolVersionFlow extends AbstractFlow<CodecContext> {
    public static final int FLOW_CODE = 0x0400;

    @Override
    public void process(CodecContext context) {
        val datagram = context.getDatagram();
        val protocolClass = context.getProtocolClass();

        VersionAssist.encode(datagram, protocolClass);

        this.nextFlow(context);
    }

    @Override
    public int getFlowCode() {
        return FLOW_CODE;
    }
}
