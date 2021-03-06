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

package org.indunet.fastproto.iot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.type.Integer16Type;
import org.indunet.fastproto.annotation.type.UInteger16Type;

/**
 * @author Deng Ran
 * @since 1.7.4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {
    @Integer16Type(0)
    int temperature;
    @UInteger16Type(2)
    int humidity;
}
