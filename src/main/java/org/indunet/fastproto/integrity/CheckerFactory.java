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

import lombok.NonNull;
import org.indunet.fastproto.annotation.Checksum;

/**
 * @author Deng Ran
 * @since 1.6.0
 */
public class CheckerFactory {
    public static Checker create(@NonNull Checksum checkSum) {
        CheckPolicy policy = checkSum.checkPolicy();
        int poly = checkSum.poly();

        switch (policy) {
            case CRC8:
                return Crc8Checker.getInstance(poly);
            case CRC8_CCITT:
                return Crc8Checker.getInstance(policy.getPoly());
            case CRC16:
                return Crc16Checker.getInstance(poly);
            case CRC16_CCITT:
                return Crc16Checker.getInstance(policy.getPoly());
            case CRC32:
                return Crc32Checker.getInstance();
        }

        return Crc16Checker.getInstance();
    }
}
