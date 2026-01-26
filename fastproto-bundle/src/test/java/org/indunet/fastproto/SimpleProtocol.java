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

import lombok.Data;
import org.indunet.fastproto.annotation.BoolType;
import org.indunet.fastproto.annotation.Int32Type;
import org.indunet.fastproto.annotation.UInt8Type;

/**
 * Simple protocol class for basic testing.
 *
 * @author Deng Ran
 * @since 4.1.0
 */
@Data
public class SimpleProtocol {
    @UInt8Type(offset = 0)
    private int id;

    @Int32Type(offset = 2)
    private int value;

    @BoolType(byteOffset = 6, bitOffset = 0)
    private boolean flag;
}
