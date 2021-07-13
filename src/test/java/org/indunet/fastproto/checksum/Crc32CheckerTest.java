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

import lombok.Builder;
import lombok.val;
import org.indunet.fastproto.annotation.EnableChecksum;
import org.indunet.fastproto.util.EncodeUtils;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.zip.CRC32;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Deng Ran
 * @since 1.6.0
 */
class Crc32CheckerTest {
    Crc32Checker checker = Crc32Checker.getInstance();

    @Test
    public void testValidate() {
        val datagram = new byte[10];
        val random = new Random(System.currentTimeMillis());

        IntStream.range(0, 10)
                .forEach(i -> datagram[i] = (byte) random.nextInt());
        CRC32 crc32 = new CRC32();

        crc32.update(datagram, 0, 6);
        long value = crc32.getValue();
        EncodeUtils.uInteger32Type(datagram, 6, value);

        assertTrue(checker.validate(datagram, TestObject.class));
    }

    @EnableChecksum(value = -4, start = 0, length = -5, checkPolicy = CheckPolicy.CRC32)
    @Builder
    public static class TestObject {

    }
}