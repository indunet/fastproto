package org.indunet.fastproto.compress;

import org.indunet.fastproto.annotation.EnableCompress;

import java.util.concurrent.ConcurrentHashMap;


/**
 * Create compressor by CompressPolicy.
 *
 * @author Deng Ran
 * @see Compressor
 * @since 1.3.0
 */
public class Compressors {
    static ConcurrentHashMap<CompressPolicy, Compressor> compressors = new ConcurrentHashMap<>();

    public static Compressor get(EnableCompress compress) {
        switch (compress.value()) {
            case GZIP:
                return compressors.computeIfAbsent(CompressPolicy.GZIP, __ -> new GzipCompressor());
            case DEFLATE:
                int level = compress.level();
                return compressors.computeIfAbsent(CompressPolicy.DEFLATE, __ -> new DeflateCompressor(level));
            default:
                return compressors.computeIfAbsent(CompressPolicy.GZIP, __ -> new GzipCompressor());
        }
    }
}
