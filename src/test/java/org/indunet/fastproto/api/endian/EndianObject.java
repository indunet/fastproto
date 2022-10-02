/*
 * Copyright 2019-2021 indunet.org
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

package org.indunet.fastproto.api.endian;

import lombok.Data;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.DefaultEndian;
import org.indunet.fastproto.annotation.type.Int16Type;

/**
 * @author Deng Ran
 * @since 1.4.0
 */
@Data
@DefaultEndian(EndianPolicy.BIG)
public class EndianObject {
    @Int16Type(offset = 0, endian = EndianPolicy.LITTLE)
    Integer little = 0x0102;
    @Int16Type(offset = 2)
    Integer big = 0x0304;
}
