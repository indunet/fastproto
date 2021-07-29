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

package org.indunet.fastproto.compress;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
class DeflateCompressorTest {
    DeflateCompressor compressor = DeflateCompressor.getInstance(1);

    @Test
    public void testCompress() {
        val before = new byte[1024];
        val random = new Random(System.currentTimeMillis());
        IntStream.range(0, 512)
                .forEach(i -> before[i] = (byte) random.nextInt());
        val after = compressor.compress(before);

        assertTrue(after.length > 0);
        assertTrue(after.length <= before.length);
    }

    @Test
    public void testUncompress() {
        val datagram = new byte[1024];
        val random = new Random(System.currentTimeMillis());
        IntStream.range(0, 512)
                .forEach(i -> datagram[i] = (byte) random.nextInt());
        val after = compressor.compress(datagram);
        val before = compressor.uncompress(after);

        assertTrue(after.length > 0);
        assertArrayEquals(datagram, before);
    }
}