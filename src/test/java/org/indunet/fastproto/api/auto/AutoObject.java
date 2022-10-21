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

package org.indunet.fastproto.api.auto;

import org.indunet.fastproto.annotation.AutoType;

/**
 * Auto object.
 *
 * @author Deng Ran
 * @since 3.7.1
 */
public class AutoObject {
    @AutoType(byteOffset = 0, bitOffset = 1)
    Boolean bool1;

    @AutoType(offset = 2)
    Byte byte8;

    @AutoType(offset = 4)
    Short short16;

    @AutoType(offset = 6)
    Integer int32;

    @AutoType(offset = 10)
    Long long64;

    public AutoObject() {

    }

    public byte[] toBytes() {
        return null;
    }
}
