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
        try (FastProtoSerializer<MyPojo> serializer = new FastProtoSerializer<>()) {
            byte[] bytes = serializer.serialize("t", pojo);
            Assertions.assertEquals(60, bytes.length);

            try (FastProtoDeserializer<MyPojo> deserializer = new FastProtoDeserializer<>()) {
                Map<String, Object> cfg = new HashMap<>();
                cfg.put(FastProtoDeserializer.CONFIG_VALUE_TYPE, MyPojo.class.getName());
                deserializer.configure(cfg, false);

                MyPojo decoded = deserializer.deserialize("t", bytes);
                assertEquals(pojo, decoded);
            }
        }
    }

    private static MyPojo sample() {
        byte[] payload = new byte[32];
        for (int i = 0; i < payload.length; i++) {
            payload[i] = (byte) (255 - i);
        }
        return new MyPojo(7, 123456789L, "Bob", payload);
    }

    @Test
    public void testSerde() {
        MyPojo pojo = sample();
        FastProtoSerde<MyPojo> serde = new FastProtoSerde<>();
        
        // Test serializer and deserializer
        Assertions.assertNotNull(serde.serializer());
        Assertions.assertNotNull(serde.deserializer());
        
        // Configure serde
        Map<String, Object> cfg = new HashMap<>();
        cfg.put(FastProtoDeserializer.CONFIG_VALUE_TYPE, MyPojo.class.getName());
        serde.configure(cfg, false);
        
        // Test serialize and deserialize
        byte[] bytes = serde.serializer().serialize("t", pojo);
        Assertions.assertEquals(60, bytes.length);
        
        MyPojo decoded = serde.deserializer().deserialize("t", bytes);
        assertEquals(pojo, decoded);
        
        // Test close
        serde.close();
    }

    private static void assertEquals(MyPojo a, MyPojo b) {
        Assertions.assertEquals(a.id, b.id);
        Assertions.assertEquals(a.timestamp, b.timestamp);
        Assertions.assertEquals(a.name, b.name);
        Assertions.assertTrue(Arrays.equals(a.payload, b.payload));
    }
} 