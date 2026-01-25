package org.indunet.fastproto.kafka;

import org.apache.kafka.common.serialization.Serializer;
import org.indunet.fastproto.FastProto;

import java.util.Map;

public class FastProtoSerializer<T> implements Serializer<T> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // no-op
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }
        return FastProto.encode(data);
    }

    @Override
    public void close() {
        // no-op
    }
} 