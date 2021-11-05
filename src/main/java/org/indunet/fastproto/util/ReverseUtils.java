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
    public static int offset(int length, int offset) {
        return offset >= 0 ? offset : length + offset;
    }

    public static int length(int datagramLength, int offset, int length) {
        int bo = offset(datagramLength, offset);

        return length >= 0 ? length : datagramLength + length - bo + 1;
    }
}
