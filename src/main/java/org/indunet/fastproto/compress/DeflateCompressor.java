package org.indunet.fastproto.compress;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author Deng Ran
 * @see Compressor
 * @since 1.3.0
 */
public class DeflateCompressor implements Compressor {
    Deflater deflater;
    Inflater inflater;

    public DeflateCompressor() {
        this(1);
    }

    public DeflateCompressor(int level) {
        this.deflater = new Deflater(level);
        this.inflater = new Inflater();
    }

    /**
     * Compress with gzip.
     *
     * @param bytes need to be compressed.
     * @return after compressing.
     */
    synchronized public byte[] compress(@NonNull byte[] bytes) {
        val out = new ByteArrayOutputStream();

        try {
            deflater.setInput(bytes);
            deflater.finish();

            val buffer = new byte[256];

            while (!deflater.finished()) {
                val n = deflater.deflate(buffer);
                out.write(buffer, 0, n);
            }
        } finally {
            deflater.end();
        }

        return out.toByteArray();
    }

    /**
     * Decompress with gzip.
     *
     * @param bytes need to be decompressed.
     * @return after decompressing.
     */
    synchronized public byte[] decompress(@NonNull byte[] bytes) {
        val out = new ByteArrayOutputStream();

        try {
            inflater.setInput(bytes);
            inflater.finished();

            val buffer = new byte[256];

            while (!inflater.finished()) {
                val n = inflater.inflate(buffer);
                out.write(buffer, 0, n);
            }
        } catch (DataFormatException e) {
            throw new DecodeException(
                    MessageFormat.format(DecodeError.FAIL_DECOMPRESS_DATAGRAM.getMessage(), this.getClass().getName()), e);
        } finally {
            inflater.end();
        }

        return out.toByteArray();
    }
}
