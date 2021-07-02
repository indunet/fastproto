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

package org.indunet.fastproto.util;

/**
 * Reverse address.
 *
 * @author Deng Ran
 * @since 2.0.0
 */
public class ReverseUtils {
    public static int byteOffset(int datagramLength, int byteOffset) {
        return byteOffset >= 0 ? byteOffset : datagramLength + byteOffset;

    }

    public static int length(int datagramLength, int byteOffset, int length) {
        int bo = byteOffset(datagramLength, byteOffset);

        return length >= 0 ? length : datagramLength + length - bo + 1;
    }
}
