package org.indunet.fastproto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Deng Ran
 * @since 1.5.0
 */
public class ProtoKafkaException extends RuntimeException {
    public ProtoKafkaException() {
        super();
    }

    public ProtoKafkaException(ProtoKafkaError error) {
        this(error.getMessage());
    }

    public ProtoKafkaException(String message) {
        super(message);
    }

    public ProtoKafkaException(String message, Throwable cause) {
        super(message, cause);
    }

    @AllArgsConstructor
    @Getter
    public enum ProtoKafkaError {
        PROTOCOL_CLASS_NOT_FOUND("protocol.class can't be found."),
        DATAGRAM_LENGTH_NOT_FOUND("datagram.length can't be found."),
        INVALID_DATAGRAM_LENGTH("Invalid datagram.length which must be String or Integer."),
        INVALID_PROTOCOL_CLASS("Invalid protocol.class which must be String or Class"),
        TYPE_NOT_MATCH("Protocol class and object don't match");

        String message;
    }
}
