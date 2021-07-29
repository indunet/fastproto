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

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CompressException;

import java.util.Arrays;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
@AllArgsConstructor
@Getter
public enum CompressPolicy {
    GZIP(0x01, "gzip"),
    DEFLATE(0x02, "deflate"),
    SANPPY(0x03, "snappy"),
    LZ4(0x04, "lz4");

    int code;
    String name;

    public static CompressPolicy byName(String name) {
        return Arrays.stream(CompressPolicy.values())
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CompressException(CodecError.INVALID_COMPRESS_POLICY));
    }
}
