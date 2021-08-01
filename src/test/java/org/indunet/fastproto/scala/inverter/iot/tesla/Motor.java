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

package org.indunet.fastproto.scala.inverter.iot.tesla;

import lombok.*;
import org.indunet.fastproto.annotation.type.AutoType;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Motor {
    @AutoType(34)
    short voltage;

    @AutoType(36)
    int current;

    @AutoType(40)
    float temperature;
}
