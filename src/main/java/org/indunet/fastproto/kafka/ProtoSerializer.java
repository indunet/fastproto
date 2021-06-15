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
