package org.indunet.fastproto.kafka;

import org.apache.kafka.common.serialization.Deserializer;
import org.indunet.fastproto.FastProto;

import java.util.Map;

@SuppressWarnings("unchecked")
public class FastProtoDeserializer<T> implements Deserializer<T> {
    public static final String CONFIG_VALUE_TYPE = "fastproto.value.type";

    private Class<?> targetType;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Object cls = configs != null ? configs.get(CONFIG_VALUE_TYPE) : null;
        if (cls instanceof String) {
            try {
                this.targetType = Class.forName((String) cls);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Class not found for fastproto.value.type: " + cls, e);
            }
        } else if (cls instanceof Class<?>) {
            this.targetType = (Class<?>) cls;
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        if (this.targetType == null) {
            throw new IllegalStateException("fastproto.value.type must be configured for FastProtoDeserializer");
        }
        Object obj = FastProto.decode(data, this.targetType);
        return (T) obj;
    }

    @Override
    public void close() {
        // no-op
    }
} 