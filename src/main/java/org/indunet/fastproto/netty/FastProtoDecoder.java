package org.indunet.fastproto.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.indunet.fastproto.FastProto;

import java.util.List;

public class FastProtoDecoder<T> extends ByteToMessageDecoder {
    private final Class<T> targetType;

    public FastProtoDecoder(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readable = in.readableBytes();
        byte[] bytes = new byte[readable];
        in.readBytes(bytes);
        T obj = FastProto.decode(bytes, targetType);
        out.add(obj);
    }
} 