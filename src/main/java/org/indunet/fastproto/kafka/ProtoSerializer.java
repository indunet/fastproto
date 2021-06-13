package org.indunet.fastproto.kafka;

import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.kafka.ProtoKafkaException.ProtoKafkaError;

import java.util.Map;

/**
 * @author Deng Ran
 * @since 1.5.0
 */
public class ProtoSerializer implements Serializer<Object> {
    protected Class<?> clazz;
    protected int length;

    @SneakyThrows(ClassNotFoundException.class)
    @Override
    public void configure(Map<String, ?> props, boolean isKey) {
        if (!props.containsKey(ProtoKafkaConfig.PROTOCOL_CLASS_KEY)) {
            throw new ProtoKafkaException(ProtoKafkaError.PROTOCOL_CLASS_NOT_FOUND);
        } else if (!props.containsKey(ProtoKafkaConfig.DATAGRAM_LENGTH_KEY)) {
            throw new ProtoKafkaException(ProtoKafkaError.DATAGRAM_LENGTH_NOT_FOUND);
        }

        Object protocolClass = props.get(ProtoKafkaConfig.PROTOCOL_CLASS_KEY);
        Object length = props.get(ProtoKafkaConfig.DATAGRAM_LENGTH_KEY);

        if (protocolClass instanceof Class) {
            this.clazz = (Class) protocolClass;
        } else if (protocolClass instanceof String) {
            this.clazz = Class.forName((String) protocolClass);
        } else {
            throw new ProtoKafkaException(ProtoKafkaError.INVALID_PROTOCOL_CLASS);
        }

        if (length instanceof String) {
            this.length = Integer.valueOf((String) length);
        } else if (length instanceof Integer) {
            this.length = (Integer) length;
        } else {
            throw new ProtoKafkaException(ProtoKafkaError.INVALID_DATAGRAM_LENGTH);
        }
    }

    @Override
    public byte[] serialize(String topic, Object object) {
        if (this.clazz.isInstance(object)) {
            return FastProto.toByteArray(object, this.length);
        } else {
            throw new ProtoKafkaException(ProtoKafkaError.TYPE_NOT_MATCH);
        }
    }
}
