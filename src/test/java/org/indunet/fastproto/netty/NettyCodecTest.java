package org.indunet.fastproto.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.domain.MyPojo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class NettyCodecTest {
    @Test
    public void testInboundDecode() {
        EmbeddedChannel ch = new EmbeddedChannel(
                new FixedLengthFrameDecoder(60),
                new FastProtoDecoder<>(MyPojo.class)
        );

        MyPojo pojo = sample();
        byte[] bytes = FastProto.encode(pojo);
        Assertions.assertEquals(60, bytes.length);

        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        ch.writeInbound(buf);

        Object out = ch.readInbound();
        Assertions.assertTrue(out instanceof MyPojo);
        MyPojo decoded = (MyPojo) out;

        assertEquals(pojo, decoded);
    }

    @Test
    public void testOutboundEncode() {
        EmbeddedChannel ch = new EmbeddedChannel(
                new FastProtoEncoder()
        );

        MyPojo pojo = sample();
        boolean accepted = ch.writeOutbound(pojo);
        Assertions.assertTrue(accepted);
        ByteBuf buf = ch.readOutbound();
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        Assertions.assertEquals(60, bytes.length);
        MyPojo decoded = FastProto.decode(bytes, MyPojo.class);
        assertEquals(pojo, decoded);
    }

    private static MyPojo sample() {
        byte[] payload = new byte[32];
        for (int i = 0; i < payload.length; i++) {
            payload[i] = (byte) i;
        }
        return new MyPojo(42, 1700000000000L, "Alice", payload);
    }

    private static void assertEquals(MyPojo a, MyPojo b) {
        Assertions.assertEquals(a.id, b.id);
        Assertions.assertEquals(a.timestamp, b.timestamp);
        Assertions.assertEquals(a.name, b.name);
        Assertions.assertTrue(Arrays.equals(a.payload, b.payload));
    }
} 