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

import org.indunet.fastproto.annotation.EnableCompress;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.CompressException;

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
                throw new CompressException(CodecError.INVALID_COMPRESS_POLICY);
        }
    }
}
