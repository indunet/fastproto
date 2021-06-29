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

package org.indunet.fastproto.integrity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Deng Ran
 * @since 1.6.0
 */
@AllArgsConstructor
@Getter
public enum CheckPolicy {
    CRC8(0xD5),
    CRC8_CCITT(0x8D),
    CRC16(0xA001),
    CRC16_CCITT(0x8408),
    CRC32(0);

    int poly;
}
