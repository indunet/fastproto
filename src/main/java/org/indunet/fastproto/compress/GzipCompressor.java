package org.indunet.fastproto.compress;

import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Compress or decompress with gzip.
 *
 * @author Deng Ran
 * @see Compressor
 * @since 1.3.0
 */
public class GzipCompressor implements Compressor {
    /**
     * Compress with gzip.
     *
     * @param bytes need to be compressed.
     * @return after compressing.
     */
    public byte[] compress(@NonNull byte[] bytes) {
        val out = new ByteArrayOutputStream();

        try (val gzip = new GZIPOutputStream(out)) {
            gzip.write(bytes);
        } catch (IOException e) {
            throw new EncodeException(
                    MessageFormat.format(EncodeError.FAIL_COMPRESS_DATAGRAM.getMessage(), this.getClass().getName()), e);
        }

        return out.toByteArray();
    }

    /**
     * Decompress with gzip.
     *
     * @param bytes need to be decompressed.
     * @return after decompressing.
     */
    public byte[] decompress(@NonNull byte[] bytes) {
        val out = new ByteArrayOutputStream();
        val in = new ByteArrayInputStream(bytes);

        try (val ungzip = new GZIPInputStream(in)) {
            val buffer = new byte[256];
            var n = 0;

            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            throw new DecodeException(
                    MessageFormat.format(DecodeError.FAIL_DECOMPRESS_DATAGRAM.getMessage(), this.getClass().getName()), e);
        }

        return out.toByteArray();
    }
}
