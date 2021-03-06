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

package org.indunet.fastproto.crypto;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * AES crypto.
 *
 * @author Deng Ran
 * @since 2.0.0
 */
public class StandardCryptoTest {
    @Test
    public void testDes() {
        byte[] bytes = new byte[100];
        val random = new Random(System.currentTimeMillis());

        IntStream.range(0, 100)
                .forEach(i -> bytes[i] = (byte) random.nextInt());
        val key = "123456".getBytes(StandardCharsets.UTF_8);
        val crypto = StandardCrypto.getInstance(CryptoPolicy.DES_ECB_PKCS5PADDING);
        val after = crypto.encrypt(key, bytes);

        assertArrayEquals(bytes, crypto.decrypt(key, after));
    }

    @Test
    public void testAes() {
        byte[] bytes = new byte[100];
        val random = new Random(System.currentTimeMillis());

        IntStream.range(0, 100)
                .forEach(i -> bytes[i] = (byte) random.nextInt());
        val key = "123456".getBytes(StandardCharsets.UTF_8);
        val crypto = StandardCrypto.getInstance(CryptoPolicy.AES_ECB_PKCS5PADDING);
        val after = crypto.encrypt(key, bytes);

        assertArrayEquals(bytes, crypto.decrypt(key, after));
    }
}