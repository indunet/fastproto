/*
 * Copyright 2019-2022 indunet.org
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

package org.indunet.fastproto.api.enumeration;

import lombok.Data;
import org.indunet.fastproto.annotation.type.EnumType;
import org.indunet.fastproto.domain.color.Color;

/**
 * String object.
 *
 * @author Deng Ran
 * @since 3.4.0
 */
@Data
public class EnumObject {
    @EnumType(offset = 0)
    Color red = Color.RED;
    @EnumType(offset = 1)
    Color yellow = Color.YELLOW;
    @EnumType(offset = 2, name = "code")
    Color green = Color.GREEN;
}
