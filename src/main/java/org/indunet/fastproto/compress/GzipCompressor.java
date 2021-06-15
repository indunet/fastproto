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
    protected final static GzipCompressor compressor = new GzipCompressor();

    protected GzipCompressor() {

    }

    public static GzipCompressor getInstance() {
        return compressor;
    }

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
