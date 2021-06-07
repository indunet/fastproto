package org.indunet.fastproto.compress;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
class DeflateCompressorTest {
    DeflateCompressor compressor = new DeflateCompressor();

    @Test
    public void testCompress() {
        val before = new byte[1024];
        val random = new Random(System.currentTimeMillis());
        IntStream.range(0, 512)
                .forEach(i -> before[i] = (byte) random.nextInt());
        val after = compressor.compress(before);

        assertTrue(after.length <= before.length);
    }

    @Test
    public void testDecompress() {
        val datagram = new byte[1024];
        val random = new Random(System.currentTimeMillis());
        IntStream.range(0, 512)
                .forEach(i -> datagram[i] = (byte) random.nextInt());
        val after = compressor.compress(datagram);
        val before = compressor.decompress(after);

        assertArrayEquals(datagram, before);
    }
}