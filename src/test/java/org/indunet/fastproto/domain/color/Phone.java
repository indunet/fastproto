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

package org.indunet.fastproto.domain.color;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.annotation.*;

/**
 * @author Deng Ran
 * @since 2.1.0
 */
@Data
@DefaultByteOrder(ByteOrder.BIG)
@DefaultBitOrder(BitOrder.LSB_0)
public class Phone {
    @EnumType(offset = 0, name = "code")
    Color backCover;

    @EnumType(offset = 1, name = "code")
    Color frontCover;

    @BoolType(byteOffset = 2, bitOffset = 0, bitOrder = BitOrder.MSB_0)
    boolean nfcEnabled;

    @AutoType(offset = 3)
    int warrantyYears;

    @AsciiType(offset = 7)
    char model;

    static final int LENGTH = 8;

    public static Phone getDefault() {
        val phone = new Phone();

        phone.setBackCover(Color.RED);
        phone.setFrontCover(Color.YELLOW);
        phone.setNfcEnabled(true);
        phone.setWarrantyYears(2);
        phone.setModel('A');

        return phone;
    }

    public static byte[] getDatagram() {
        val datagram = new byte[LENGTH];

        datagram[0] = (byte) Color.RED.getCode();
        datagram[1] = (byte) Color.YELLOW.getCode();
        datagram[2] = 0x01;                      // nfcEnabled
        datagram[3] = 0x00;
        datagram[4] = 0x00;
        datagram[5] = 0x00;
        datagram[6] = 0x02;                      // warrantyYears big-endian
        datagram[7] = 'A';

        return datagram;
    }

    public static int getLength() {
        return LENGTH;
    }
}
