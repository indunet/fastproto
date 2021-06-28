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
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.iot.Weather;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Netty decoder.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class ProtoEncoderTest {
    @Test
    public void testEncode() {
        Weather weather = Weather.newInstance();
        EmbeddedChannel channel = new EmbeddedChannel(new ProtoEncoder(30));

        assertTrue(channel.writeOutbound(weather));
        assertTrue(channel.finish());

        ByteBuf buf = channel.readOutbound();
        val cache = new byte[34];
        buf.getBytes(buf.readerIndex(), cache);

        val expected = FastProto.toByteArray(weather, 30);
        assertArrayEquals(expected, cache);
    }
}
