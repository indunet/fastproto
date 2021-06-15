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

import com.salesforce.kafka.test.junit5.SharedKafkaTestResource;
import lombok.val;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.indunet.fastproto.iot.Weather;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @since 1.5.2
 */
public class KafkaTest {
    @RegisterExtension
    public static final SharedKafkaTestResource sharedKafkaTestResource = new SharedKafkaTestResource();

    @Test
    public void testKafka() {
        Properties props = new Properties();

        props.put("bootstrap.servers", sharedKafkaTestResource.getKafkaConnectString());
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", ProtoKafkaConfig.SERIALIZER_NAME_VALUE);

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", ProtoKafkaConfig.DESERIALIZER_NAME_VALUE);
        props.put("group.id", "1");

        props.put(ProtoKafkaConfig.PROTOCOL_CLASS_KEY, Weather.class);
        props.put(ProtoKafkaConfig.DATAGRAM_LENGTH_KEY, 26);

        val producer = new KafkaProducer<String, Weather>(props);
        val consumer = new KafkaConsumer<String, Weather>(props);
        Weather weather = Weather.builder()
                .id(101)
                .time(new Timestamp(System.currentTimeMillis()))
                .humidity(85)
                .temperature(-15)
                .pressure(13)
                .humidityValid(true)
                .temperatureValid(true)
                .pressureValid(true)
                .build();
        val record = new ProducerRecord<String, Weather>("fastproto.weather", weather);
        Runnable task = () -> {
            while (true) {
                producer.send(record);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();

        consumer.subscribe(Collections.singletonList("fastproto.weather"));

        ConsumerRecords<String, Weather> records = consumer.poll(Duration.ofSeconds(10));
        consumer.commitSync();

        if (records.isEmpty()) {
            assertTrue(false);
        } else {
            for (val r : records) {
                assertEquals(r.value().toString(), weather.toString());
                return;
            }
        }

        thread.interrupt();
    }
}
