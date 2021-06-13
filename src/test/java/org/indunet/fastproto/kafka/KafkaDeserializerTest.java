package org.indunet.fastproto.kafka;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.iot.Weather;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Deng Ran
 * @since 1.5.2
 */
public class KafkaDeserializerTest {
    ProtoSerializer serializer = new ProtoSerializer();

    @Test
    void testSerialize() {
        val props = new Properties();

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", ProtoKafkaConfig.SERIALIZER_NAME_VALUE);

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", ProtoKafkaConfig.DESERIALIZER_NAME_VALUE);

        props.put(ProtoKafkaConfig.PROTOCOL_CLASS_KEY, Weather.class);
        props.put(ProtoKafkaConfig.DATAGRAM_LENGTH_KEY, 26);

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
        serializer.configure(map, false);

        assertArrayEquals(serializer.serialize("device0", weather), FastProto.toByteArray(weather, 26));
    }
}
