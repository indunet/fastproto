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

package org.indunet.fastproto.scala.iot.inverter.color;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.annotation.type.EnumType;

/**
 * @author Deng Ran
 * @since 2.1.0
 */
@Data
public class Phone {
    @EnumType(value = 0, fieldName = "code")
    Color backCover;

    @EnumType(value = 1, fieldName = "code")
    Color frontCover;

    transient static final int LENGTH = 10;

    public static Phone getDefault() {
        val phone = new Phone();

        phone.setBackCover(Color.RED);
        phone.setFrontCover(Color.YELLOW);

        return phone;
    }

    public static byte[] getDatagram() {
        val datagram = new byte[LENGTH];

        datagram[0] = (byte) Color.RED.getCode();
        datagram[1] = (byte) Color.YELLOW.getCode();

        return datagram;
    }

    public static int getLength() {
        return LENGTH;
    }
}
