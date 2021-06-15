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
        PROTOCOL_CLASS_NOT_FOUND("protocol.class cannot be found."),
        DATAGRAM_LENGTH_NOT_FOUND("datagram.length cannot be found."),
        INVALID_DATAGRAM_LENGTH("Invalid datagram.length which must be String or Integer."),
        INVALID_PROTOCOL_CLASS("Invalid protocol.class which must be String or Class"),
        TYPE_NOT_MATCH("Protocol class and object don't match");

        String message;
    }
}
