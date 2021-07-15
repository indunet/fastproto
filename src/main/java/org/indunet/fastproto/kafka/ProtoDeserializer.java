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
        if (!props.containsKey(ProtoKafkaHelper.PROTOCOL_CLASS_KEY)) {
            throw new ProtoKafkaException(ProtoKafkaError.DATAGRAM_LENGTH_NOT_FOUND);
        }

        Object protocolClass = props.get(ProtoKafkaHelper.PROTOCOL_CLASS_KEY);

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
