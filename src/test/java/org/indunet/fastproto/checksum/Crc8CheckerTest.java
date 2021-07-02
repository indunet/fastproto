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

package org.indunet.fastproto.checksum;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.encoder.EncodeUtils;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Deng Ran
 * @since 1.6.3
 */
public class Crc8CheckerTest {

    @Test
    public void testCrc8() {
        Crc8Checker checker = Crc8Checker.getInstance();
        val datagram = new byte[10];
        val random = new Random(System.currentTimeMillis());

        IntStream.range(0, 10)
                .forEach(i -> datagram[i] = (byte) random.nextInt());
        int value = checker.getValue(datagram, 0, 9);
        EncodeUtils.uInteger8Type(datagram, 9, value);

        assertTrue(checker.validate(datagram, TestObject1.class));
    }

    @Checksum(value = -1, start = 0, length = -2, checkPolicy = CheckPolicy.CRC8)
    public static class TestObject1 {

    }

    @Test
    public void testCrc8Ccitt() {
        val checker = Crc8Checker.getInstance(CheckPolicy.CRC8_CCITT.getPoly());
        val datagram = new byte[10];
        val random = new Random(System.currentTimeMillis());

        IntStream.range(0, 10)
                .forEach(i -> datagram[i] = (byte) random.nextInt());
        int value = checker.getValue(datagram, 0, 9);
        EncodeUtils.uInteger8Type(datagram, 9, value);

        assertTrue(checker.validate(datagram, TestObject.class));
    }

    @Checksum(value = -1, start = 0, length = -1, checkPolicy = CheckPolicy.CRC8_CCITT)
    public static class TestObject {

    }

    @Test
    public void testtoByteArray() {
        val testObject = new TestObject();
        byte[] datagram = FastProto.toByteArray(testObject, 30);

        assertNotNull(datagram);
    }
}
