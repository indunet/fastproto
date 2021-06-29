package org.indunet.fastproto.flow.encode;

import lombok.val;
import org.indunet.fastproto.VersionAssist;
import org.indunet.fastproto.flow.AbstractFlow;
import org.indunet.fastproto.flow.CodecContext;
import org.indunet.fastproto.integrity.CheckerUtils;

public class InferLengthFlow extends AbstractFlow<CodecContext> {
    public static final int FLOW_CODE = 0x0100;

    @Override
    public void process(CodecContext context) {
        val assist = context.getTypeAssist();
        int length = assist.getMaxLength();
        length += CheckerUtils.getSize(context.getProtocolClass());
        length += VersionAssist.getSize(context.getProtocolClass());

        context.setDatagram(new byte[length]);
        this.nextFlow(context);
    }

    @Override
    public int getFlowCode() {
        return FLOW_CODE;
    }
}
