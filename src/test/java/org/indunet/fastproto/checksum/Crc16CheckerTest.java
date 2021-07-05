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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.EnableChecksum;
import org.indunet.fastproto.encoder.EncodeUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @since 1.6.3
 */
class Crc16CheckerTest {
    Crc16Checker checker = Crc16Checker.getInstance();

    @Test
    public void testValidate() {
        val datagram = new byte[10];
        val random = new Random(System.currentTimeMillis());

        IntStream.range(0, 10)
                .forEach(i -> datagram[i] = (byte) random.nextInt());
        int value = checker.getValue(datagram, 0, 8);

        String tmp = CRC16Util.getCRC(Arrays.copyOfRange(datagram, 0, 8));
        int x = Integer.parseInt(tmp, 16);
        EncodeUtils.uInteger16Type(datagram, 8, EndianPolicy.BIG, value);
        assertEquals(value, x);
        assertTrue(checker.validate(datagram, TestObject.class));
    }

    @EnableChecksum(value = -2, checkPolicy = CheckPolicy.CRC16, start = 0, length = -3, endianPolicy = EndianPolicy.BIG)
    public static class TestObject {

    }

    @Test
    public void testToByteArray() {
        val testObject = new TestObject();
        val datagram = FastProto.toByteArray(testObject, 30);

        assertNotNull(datagram);
    }
}