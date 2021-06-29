package org.indunet.fastproto.flow.encode;

import lombok.val;
import org.indunet.fastproto.CodecFeature;
import org.indunet.fastproto.annotation.EnableCompress;
import org.indunet.fastproto.compress.CompressorFactory;
import org.indunet.fastproto.flow.AbstractFlow;
import org.indunet.fastproto.flow.CodecContext;

public class CompressFlow extends AbstractFlow<CodecContext> {
    public static final int FLOW_CODE = 0x1000;

    @Override
    public void process(CodecContext context) {
        boolean enableCompress =
                    (context.getCodecFeature() & CodecFeature.IGNORE_ENABLE_COMPRESS) == 0;
        Class<?> protocolClass = context.getProtocolClass();
        byte[] datagram = context.getDatagram();

        if (enableCompress && protocolClass.isAnnotationPresent(EnableCompress.class)) {
            val annotation = protocolClass.getAnnotation(EnableCompress.class);
            val compressor = CompressorFactory.create(annotation);

            context.setDatagram(compressor.compress(datagram));
        }

        this.nextFlow(context);
    }

    public int getFlowCode() {
        return FLOW_CODE;
    }
}
