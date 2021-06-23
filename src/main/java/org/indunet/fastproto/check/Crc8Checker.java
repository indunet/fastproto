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

package org.indunet.fastproto.check;

import lombok.val;
import org.indunet.fastproto.annotation.CheckSum;
import org.indunet.fastproto.annotation.type.UInteger8Type;
import org.indunet.fastproto.decoder.DecodeUtils;
import org.indunet.fastproto.encoder.EncodeUtils;
import org.indunet.fastproto.exception.DecodeException;

/**
 * @author Deng Ran
 * @since 1.6.3
 */
public class Crc8Checker implements Checker {
    protected final static Crc8Checker crc8Checker = new Crc8Checker();
    protected int poly = 0xFF;

    protected Crc8Checker() {

    }

    public static Crc8Checker getInstance() {
        return crc8Checker;
    }

    @Override
    public boolean validate(byte[] datagram, Class<?> protocolClass) {
        if (!protocolClass.isAnnotationPresent(CheckSum.class)) {
            return true;
        }

        val checkSum = protocolClass.getAnnotation(CheckSum.class);
        int byteOffset = checkSum.byteOffset();
        int length = checkSum.length();

        int actual = this.getValue(datagram, byteOffset, length);

        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;
        int l = length >= 0 ? length : datagram.length + length - bo;
        int expected = DecodeUtils.uInteger8Type(datagram, bo + l);

        return actual == expected;
    }

    @Override
    public void setValue(byte[] datagram, Class<?> protocolClass) {
        if (!protocolClass.isAnnotationPresent(CheckSum.class)) {
            return;
        }

        val checkSum = protocolClass.getAnnotation(CheckSum.class);
        int byteOffset = checkSum.byteOffset();
        int length = checkSum.length();

        this.setValue(datagram, byteOffset, length);
    }

    @Override
    public int getSize() {
        return UInteger8Type.SIZE;
    }

    public void setValue(byte[] datagram, int byteOffset, int length) {
        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;
        int l = length >= 0 ? length : datagram.length + length - bo;
        int value = this.getValue(datagram, byteOffset, length);

        EncodeUtils.uInteger8Type(datagram, bo + l, value);
    }

    public int getValue(byte[] datagram, int byteOffset, int length) {
        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;
        int l = length >= 0 ? length : datagram.length + length - bo;

        if (bo < 0) {
            throw new DecodeException(DecodeException.DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (l < 0) {
            throw new DecodeException(DecodeException.DecodeError.ILLEGAL_PARAMETER);
        } else if (bo + length > datagram.length) {
            throw new DecodeException(DecodeException.DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        byte crc8 = 0;

        for (int i = 0; i < l; i ++) {
            crc8 ^= datagram[bo + i];

            for (int j = 0; j < 8; j ++) {
                if ((crc8 & 0x80) != 0) {
                    crc8 <<= 1;
                    crc8 ^= poly;
                } else {
                    crc8 <<= 1;
                }
            }
        }

        return crc8 & 0xFF;
    }
}
