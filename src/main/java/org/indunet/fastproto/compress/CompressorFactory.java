package org.indunet.fastproto.compress;

import org.indunet.fastproto.annotation.EnableCompress;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.CodecException.CodecError;

/**
 * Create compressor by CompressPolicy.
 *
 * @author Deng Ran
 * @see Compressor
 * @since 1.3.0
 */
public class CompressorFactory {
    public static Compressor create(EnableCompress annotation) {
        switch (annotation.value()) {
            case GZIP:
                return GzipCompressor.getInstance();
            case DEFLATE:
                return DeflateCompressor.getInstance(annotation.level());
            default:
                throw new CodecException(CodecError.INVALID_COMPRESS_POLICY);
        }
    }
}
