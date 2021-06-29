package org.indunet.fastproto.flow.encode;

import lombok.val;
import org.indunet.fastproto.annotation.DataIntegrity;
import org.indunet.fastproto.flow.AbstractFlow;
import org.indunet.fastproto.flow.CodecContext;
import org.indunet.fastproto.integrity.Checker;
import org.indunet.fastproto.integrity.CheckerFactory;

public class WriteChecksumFlow extends AbstractFlow<CodecContext> {
    public static final int FLOW_CODE = 0x0800;

    @Override
    public void process(CodecContext context) {
        val object = context.getObject();
        val datagram = context.getDatagram();

        // Check sum.
        if (object.getClass().isAnnotationPresent(DataIntegrity.class)) {
            DataIntegrity checkSum = object.getClass().getAnnotation(DataIntegrity.class);
            Checker checker = CheckerFactory.create(checkSum);

            checker.setValue(datagram, object.getClass());
        }

        this.nextFlow(context);
    }

    @Override
    public int getFlowCode() {
        return FLOW_CODE;
    }
}
