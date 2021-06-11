package org.indunet.fastproto.kafka;

/**
 * @author Deng Ran
 * @since 1.5.0
 */
public final class ProtoKafkaConfig {
    public static final String PROTOCOL_CLASS_KEY = "protocol.class";
    public static final String DATAGRAM_LENGTH_KEY = "datagram.length";
    public static final String SERIALIZER_NAME_VALUE = ProtoSerializer.class.getName();
    public static final String DESERIALIZER_NAME_VALUE = ProtoDeserializer.class.getName();
}
