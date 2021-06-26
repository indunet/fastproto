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

import com.sun.istack.internal.NotNull;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.Optional;

/**
 * Netty decoder.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class ProtoDecoder extends ByteToMessageDecoder {
    Class<?> protocolClass;
    Optional<Integer> length;

    public ProtoDecoder(@NotNull Class<?> protocolClass) {
        this.protocolClass = protocolClass;
        this.length = Optional.empty();
    }

    public ProtoDecoder(@NotNull Class<?> protocolClass, int length) {
        if (length <= 0) {
            throw new ProtoNettyException();
        }

        this.protocolClass = protocolClass;
        this.length = Optional.empty();
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws Exception {
        // HttpRequestDecoder
    }
}
