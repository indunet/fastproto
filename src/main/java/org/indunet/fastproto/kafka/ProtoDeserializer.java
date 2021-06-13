package org.indunet.fastproto.kafka;

import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Deserializer;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.kafka.ProtoKafkaException.ProtoKafkaError;

import java.util.Map;

/**
 * @author Deng Ran
 * @since 1.5.0
 */
public class ProtoDeserializer implements Deserializer<Object> {
    protected Class<?> protocolClass;

    @SneakyThrows
    @Override
    public void configure(Map props, boolean isKey) {
        if (!props.containsKey(ProtoKafkaConfig.PROTOCOL_CLASS_KEY)) {
            throw new ProtoKafkaException(ProtoKafkaError.DATAGRAM_LENGTH_NOT_FOUND);
        }

        Object protocolClass = props.get(ProtoKafkaConfig.PROTOCOL_CLASS_KEY);

        if (protocolClass instanceof Class) {
            this.protocolClass = (Class) protocolClass;
        } else if (protocolClass instanceof String) {
            this.protocolClass = Class.forName((String) protocolClass);
        } else {
            throw new ProtoKafkaException(ProtoKafkaError.INVALID_PROTOCOL_CLASS);
        }
    }

    @Override
    public Object deserialize(String topic, byte[] bytes) {
        return FastProto.parseFrom(bytes, this.protocolClass);
    }
}
