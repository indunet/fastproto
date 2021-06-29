package org.indunet.fastproto.flow.decode;

import lombok.val;
import org.indunet.fastproto.annotation.DataIntegrity;
import org.indunet.fastproto.exception.CheckSumException;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.flow.AbstractFlow;
import org.indunet.fastproto.flow.CodecContext;
import org.indunet.fastproto.integrity.Checker;
import org.indunet.fastproto.integrity.CheckerFactory;

public class VerifyChecksumFlow extends AbstractFlow<CodecContext> {
    public static final int FLOW_CODE = 0x0002;

    @Override
    public void process(CodecContext context) {
        val protocolClass = context.getProtocolClass();
        val datagram = context.getDatagram();

        if (protocolClass.isAnnotationPresent(DataIntegrity.class)) {
            Checker checker = CheckerFactory.create(protocolClass.getAnnotation(DataIntegrity.class));

            if (!checker.validate(datagram, protocolClass)) {
                throw new CheckSumException(CodecError.ILLEGAL_CHECK_SUM);
            }
        }

        this.nextFlow(context);
    }

    @Override
    public int getFlowCode() {
        return FLOW_CODE;
    }
}
