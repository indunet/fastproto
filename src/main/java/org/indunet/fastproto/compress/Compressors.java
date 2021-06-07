package org.indunet.fastproto.compress;

import org.indunet.fastproto.annotation.Compress;

import java.util.concurrent.ConcurrentHashMap;
import static org.indunet.fastproto.compress.CompressPolicy.*;


/**
 * Create compressor by CompressPolicy.
 *
 * @author Deng Ran
 * @see Compressor
 * @since 1.3.0
 */
public class Compressors {
    static ConcurrentHashMap<CompressPolicy, Compressor> compressors = new ConcurrentHashMap<>();

    public static Compressor get(Compress compress) {
        switch (compress.value()) {
            case GZIP:
                return compressors.computeIfAbsent(CompressPolicy.GZIP, __ -> new GzipCompressor());
            case DEFLATE:
                int level = compress.level();
                return compressors.computeIfAbsent(CompressPolicy.GZIP, __ -> new DeflateCompressor(level));
            default:
                return null;
        }
    }
}
