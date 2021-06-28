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
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.indunet.fastproto.iot.Sensor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Netty decoder.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class ProtoDecoderTest {
    @Test
    public void testDecode() {
        Sensor sensor = new Sensor(23, 86);
        ByteBuf buf = Unpooled.buffer();
        buf.writeShortLE(sensor.getTemperature());
        buf.writeShortLE(sensor.getHumidity());

        EmbeddedChannel channel = new EmbeddedChannel(new ProtoDecoder(Sensor.class));

        assertTrue(channel.writeInbound(buf));
        assertTrue(channel.finish());

        assertEquals(sensor.toString(), ((Sensor) channel.readInbound()).toString());
    }
}
