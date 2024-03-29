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

package org.indunet.fastproto.domain.tesla;

import lombok.*;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;

import java.sql.Timestamp;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DefaultByteOrder(ByteOrder.LITTLE)
public class Tesla {
    @Int64Type(offset = 0)
    Long id;

    @TimeType(offset = 8)
    Timestamp time;

    @FloatType(offset = 16)
    float speed;

    @BoolType(byteOffset = 20, bitOffset = 0)
    boolean active;

    Battery battery;
    Motor motor;
}
