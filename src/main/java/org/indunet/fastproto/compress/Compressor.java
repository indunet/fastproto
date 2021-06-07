package org.indunet.fastproto.compress;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
public interface Compressor {
    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
