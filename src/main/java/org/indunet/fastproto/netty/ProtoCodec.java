/*
 * Copyright 2019-2021 indunet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.indunet.fastproto.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.indunet.fastproto.FastProto;

import java.util.List;
import java.util.Optional;

/**
 * Netty codec.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class ProtoCodec extends ByteToMessageCodec {
    Class<?> protocolClass;
    Optional<Integer> length;

    @Override
    protected void encode(ChannelHandlerContext context, Object object, ByteBuf byteBuf) throws Exception {
        byte[] bytes;

        if (this.length.isPresent()) {
            bytes = FastProto.toByteArray(object);
        } else {
            bytes = FastProto.toByteArray(object);
        }

        byteBuf.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List list) throws Exception {

    }
}
