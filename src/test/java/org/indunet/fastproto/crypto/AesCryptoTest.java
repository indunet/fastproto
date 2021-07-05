package org.indunet.fastproto.crypto;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AesCryptoTest {
    @Test
    public void test() {
        byte[] bytes = new byte[100];
        val random = new Random(System.currentTimeMillis());

        IntStream.range(0, 100)
                .forEach(i -> {
                    bytes[i] = (byte) random.nextInt();
                });
        val key = "1234567890abcdef".getBytes(StandardCharsets.UTF_8);
        val crypto = AesCrypto.getInstance();
        val after = crypto.encrypt(key, bytes);

        assertArrayEquals(bytes, crypto.decrypt(key, after));
    }
}