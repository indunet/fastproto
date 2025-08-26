package org.indunet.fastproto.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.indunet.fastproto.FastProto;

public class FastProtoEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg == null) {
            return;
        }
        byte[] bytes = FastProto.encode(msg);
        out.writeBytes(bytes);
    }
} 