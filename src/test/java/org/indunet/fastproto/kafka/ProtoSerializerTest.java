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

package org.indunet.fastproto.kafka;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.scala.inverter.iot.Weather;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Deng Ran
 * @since 1.5.2
 */
public class ProtoSerializerTest {
    protected ProtoDeserializer deserializer = new ProtoDeserializer();

    @Test
    public void testDeserialize() {
        val props = new Properties();

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", ProtoKafkaHelper.SERIALIZER_NAME_VALUE);

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", ProtoKafkaHelper.DESERIALIZER_NAME_VALUE);

        props.put(ProtoKafkaHelper.PROTOCOL_CLASS_KEY, Weather.class);
        props.put(ProtoKafkaHelper.DATAGRAM_LENGTH_KEY, 26);

        val map = new HashMap<String, Object>();
        Weather weather = Weather.builder()
                .id(101)
                .time(new Timestamp(System.currentTimeMillis()))
                .humidity(85)
                .temperature(-15)
                .pressure(13)
                .humidityValid(true)
                .temperatureValid(true)
                .pressureValid(true)
                .build();
        props.forEach((key, value) -> map.put(key.toString(), value));
        deserializer.configure(map, false);

        assertEquals(deserializer.deserialize("device0", FastProto.toByteArray(weather, 26)), weather);
    }
}
