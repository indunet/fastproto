package org.indunet.fastproto.compress;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.CodecException.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author Deng Ran
 * @see Compressor
 * @since 1.3.0
 */
public class DeflateCompressor implements Compressor {
    protected final static ConcurrentHashMap<Integer, DeflateCompressor> compressors = new ConcurrentHashMap<>();
    protected final static int MAX_LEVEL = 9;
    protected final static int MIN_LEVEL = 0;
    protected int level = 0;

    protected DeflateCompressor(int level) {
        this.level = level;
    }

    public static DeflateCompressor getInstance(int level) {
        if (level < MIN_LEVEL || level > MAX_LEVEL) {
            throw new CodecException(CodecError.INVALID_COMPRESS_POLICY);
        }

        return compressors.computeIfAbsent(level, __ -> new DeflateCompressor(level));
    }

    /**
     * Compress with gzip.
     *
     * @param bytes need to be compressed.
     * @return after compressing.
     */
    @SneakyThrows
    synchronized public byte[] compress(@NonNull byte[] bytes) {
        val deflater = new Deflater(this.level);
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
            out.close();
        }

        return out.toByteArray();
    }

    /**
     * Decompress with gzip.
     *
     * @param bytes need to be decompressed.
     * @return after decompressing.
     */
    @SneakyThrows
    synchronized public byte[] decompress(@NonNull byte[] bytes) {
        val inflater = new Inflater();
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
            throw new DecodeException(MessageFormat.format(
                    DecodeError.FAIL_DECOMPRESS_DATAGRAM.getMessage(), this.getClass().getName()), e);
        } finally {
            inflater.end();
            out.close();
        }

        return out.toByteArray();
    }
}
