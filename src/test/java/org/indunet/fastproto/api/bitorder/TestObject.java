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

package org.indunet.fastproto.api.bitorder;

import lombok.Data;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.annotation.BoolType;
import org.indunet.fastproto.annotation.DefaultBitOrder;

import static org.indunet.fastproto.annotation.BoolType.BIT_2;
import static org.indunet.fastproto.annotation.BoolType.BIT_6;

/**
 * Test object of bit order.
 *
 * @author Deng Ran
 * @since 3.9.2
 */
@Data
@DefaultBitOrder(BitOrder.MSB_0)
public class TestObject {
    @BoolType(byteOffset = 0, bitOffset = BIT_2, bitOrder = BitOrder.LSB_0)
    Boolean lsb = true;
    @BoolType(byteOffset = 1, bitOffset = BIT_6)
    Boolean msb = true;

    public byte[] toBytes() {
        return new byte[] {0x04, 0x02};
    }
}
