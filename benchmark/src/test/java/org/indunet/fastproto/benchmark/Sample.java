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

package org.indunet.fastproto.benchmark;

import org.indunet.fastproto.annotation.*;

/**
 * @author Deng Ran
 * @since 1.4.0
 */
public class Sample {
    @BoolType(byteOffset = 0, bitOffset = 1)
    Boolean bool1 = true;

    @Int8Type(offset = 1)
    Byte byte8 = 12;

    @Int16Type(offset = 2)
    Short short16 = 128;

    @Int32Type(offset = 4)
    Integer int32 = 34;

    @Int64Type(offset = 8)
    Long long64 = 90l;

    @FloatType(offset = 16)
    Float float32 = 60.21f;

    @DoubleType(offset = 20)
    Double double64 = 90.11;

    @Int8Type(offset = 28)
    Integer int8 = 65;

    @Int16Type(offset = 30)
    Integer int16 = 9921;

    @UInt8Type(offset = 32)
    Integer uint8 = 232;

    @UInt16Type(offset = 34)
    Integer uint16 = 1231;

    @UInt32Type(offset = 36)
    Long uint32 = 123;

    @BinaryType(offset = 40, length = 10)
    byte[] bytes;

    @StringType(offset = 50, length = 6)
    String str;

    public byte[] toBytes() {

    }
}
