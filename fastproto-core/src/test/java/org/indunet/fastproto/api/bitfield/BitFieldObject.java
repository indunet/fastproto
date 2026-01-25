/*
 * Copyright 2019-2025 indunet.org
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

package org.indunet.fastproto.api.bitfield;

import lombok.Data;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.BitFieldType;
import org.indunet.fastproto.annotation.DefaultBitOrder;
import org.indunet.fastproto.annotation.DefaultByteOrder;
import org.indunet.fastproto.annotation.UInt8Type;

/**
 * Test object for bit field API with dynamic offset/length.
 */
@Data
@DefaultByteOrder(ByteOrder.LITTLE)
@DefaultBitOrder(BitOrder.LSB_0)
public class BitFieldObject {
    @UInt8Type(offset = 0)
    int pos = 2;

    @UInt8Type(offset = 1)
    int width = 9;

    @BitFieldType(offsetRef = "$pos", bitOffset = 3, lengthRef = "$width", byteOrder = ByteOrder.LITTLE, bitOrder = BitOrder.LSB_0)
    int value = 0x1A3;      // 9-bit value placed starting at bitOffset=3, spans bytes 2-3

    @BitFieldType(offset = 4, bitOffset = 0, length = 6, byteOrder = ByteOrder.BIG, bitOrder = BitOrder.MSB_0)
    int header = 0b111001;  // occupies bits 7..2 of byte 4

    public byte[] toBytes() {
        return new byte[] {
                0x02,       // pos
                0x09,       // width
                0x18,       // value bits [3..7] -> 0b00011000
                0x0D,       // value bits [8..11] -> 0b00001101
                (byte) 0xE4 // header 6 bits MSB_0 + BIG -> 0b11100100
        };
    }
}

