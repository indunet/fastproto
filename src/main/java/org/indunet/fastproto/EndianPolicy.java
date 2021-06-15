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

package org.indunet.fastproto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.CodecException.CodecError;

import java.util.Arrays;

/**
 * Endian policy.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum EndianPolicy {
    BIG(0x01, "Big"),
    LITTLE(0x02, "Little");

    int code;
    String name;

    public static EndianPolicy byName(String name) {
        return Arrays.stream(EndianPolicy.values())
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CodecException(CodecError.INVALID_ENDIAN_POLICY));
    }
}
