/**
 * Copyright 2019-2023 indunet.org
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

package org.indunet.fastproto.util;

import lombok.val;
import org.indunet.fastproto.exception.CodecException;

/**
 * Reverse utils which can reverse byte offset or length.
 *
 * @author Deng Ran
 * @since 3.10.2
 */
public class ReverseUtils {
    public static int reverse(byte[] bytes, int offset) {
        val o = offset >= 0 ? offset : bytes.length + offset;

        if (o >= 0) {
            return o;
        } else {
            throw new CodecException(String.format("Illegal offset %d", o));
        }
    }

    public static int reverse(byte[] bytes, int offset, int length) {
        int o = reverse(bytes, offset);
        int l = length >= 0 ? length : bytes.length + length - o + 1;

        if (l > 0) {
            return l;
        } else {
            throw new CodecException(String.format("Illegal length %d", l));
        }
    }
}
