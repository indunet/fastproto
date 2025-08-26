package org.indunet.fastproto.kafka;

import org.indunet.fastproto.domain.MyPojo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KafkaSerdeTest {
    @Test
    public void testSerializerDeserializer() {
        MyPojo pojo = sample();
        FastProtoSerializer<MyPojo> serializer = new FastProtoSerializer<>();
        byte[] bytes = serializer.serialize("t", pojo);
        Assertions.assertEquals(60, bytes.length);

        FastProtoDeserializer<MyPojo> deserializer = new FastProtoDeserializer<>();
        Map<String, Object> cfg = new HashMap<>();
        cfg.put(FastProtoDeserializer.CONFIG_VALUE_TYPE, MyPojo.class.getName());
        deserializer.configure(cfg, false);

        MyPojo decoded = deserializer.deserialize("t", bytes);
        assertEquals(pojo, decoded);
    }

    private static MyPojo sample() {
        byte[] payload = new byte[32];
        for (int i = 0; i < payload.length; i++) {
            payload[i] = (byte) (255 - i);
        }
        return new MyPojo(7, 123456789L, "Bob", payload);
    }

    private static void assertEquals(MyPojo a, MyPojo b) {
        Assertions.assertEquals(a.id, b.id);
        Assertions.assertEquals(a.timestamp, b.timestamp);
        Assertions.assertEquals(a.name, b.name);
        Assertions.assertTrue(Arrays.equals(a.payload, b.payload));
    }
} 