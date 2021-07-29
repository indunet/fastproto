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

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;

import java.util.Arrays;

/**
 * Compress or decompress with lz4.
 *
 * @author Deng Ran
 * @see Compressor
 * @since 2.4.0
 */
public class Lz4Compressor implements Compressor{
    @Override
    public byte[] compress(byte[] bytes) {
        LZ4Factory lz4Factory = LZ4Factory.safeInstance();
        LZ4Compressor fastCompressor = lz4Factory.fastCompressor();
        int maxCompressedLength = fastCompressor.maxCompressedLength(bytes.length);
        byte[] cache = new byte[maxCompressedLength];
        int compressedLength = fastCompressor.compress(bytes, 0, bytes.length, cache, 0, maxCompressedLength);

        return Arrays.copyOf(cache, compressedLength);
    }

    @Override
    public byte[] uncompress(byte[] bytes) {
        LZ4Factory lz4Factory = LZ4Factory.safeInstance();
        LZ4SafeDecompressor decompressor2 = lz4Factory.safeDecompressor();
        byte[] cache = new byte[bytes.length * 4];
        int decompressedLength = decompressor2.decompress(bytes, 0, bytes.length, cache, 0);

        return Arrays.copyOf(cache, decompressedLength);
    }
}
