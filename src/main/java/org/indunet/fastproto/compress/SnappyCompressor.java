/*
 * Copyright 2019-2021 indunet.org
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

import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CompressException;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * Compress or decompress with snappy.
 *
 * @author Deng Ran
 * @see Compressor
 * @since 2.4.0
 */
public class SnappyCompressor implements Compressor{
    @Override
    public byte[] compress(byte[] bytes) {
        try {
            return Snappy.compress(bytes);
        } catch (IOException e) {
            throw new CompressException(
                    MessageFormat.format(CodecError.FAIL_COMPRESS_DATAGRAM.getMessage(), this.getClass().getName()), e);
        }
    }

    @Override
    public byte[] uncompress(byte[] bytes) {
        try {
            return Snappy.uncompress(bytes);
        } catch (IOException e) {
            throw new CompressException(
                    MessageFormat.format(CodecError.FAIL_UNCOMPRESS_DATAGRAM.getMessage(), this.getClass().getName()), e);
        }
    }
}
