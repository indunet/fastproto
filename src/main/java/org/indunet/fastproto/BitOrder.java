/*
 * Copyright 2019-2024 indunet.org
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

/**
 * Bit order policy.
 *
 * @author Deng Ran
 * @since 3.9.0
 */
@Getter
@AllArgsConstructor
public enum BitOrder {
    LSB_0(0x01, "LSB_0"),
    MSB_0(0x02, "MSB_0");

    final int code;
    final String name;
}
