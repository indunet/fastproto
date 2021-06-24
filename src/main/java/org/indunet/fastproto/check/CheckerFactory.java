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

package org.indunet.fastproto.check;

import lombok.NonNull;
import org.indunet.fastproto.annotation.CheckSum;

/**
 * @author Deng Ran
 * @since 1.6.0
 */
public class CheckerFactory {
    public static Checker create(@NonNull CheckSum checkSum) {
        CheckPolicy policy = checkSum.value();
        int poly = checkSum.poly();

        switch (checkSum.value()) {
            case CRC8:
                return Crc8Checker.getInstance(poly);
            case CRC16:
                return Crc16Checker.getInstance(poly);
            case CRC32:
                return Crc32Checker.getInstance();
        }

        return Crc16Checker.getInstance();
    }
}
