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

import lombok.Getter;
import lombok.val;
import org.indunet.fastproto.annotation.CheckSum;
import org.indunet.fastproto.annotation.type.UInteger8Type;
import org.indunet.fastproto.decoder.DecodeUtils;
import org.indunet.fastproto.encoder.EncodeUtils;
import org.indunet.fastproto.exception.DecodeException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Deng Ran
 * @since 1.6.3
 */
@Getter
public class Crc8Checker implements Checker {
    protected static final Map<Integer, Crc8Checker> checkers = new ConcurrentHashMap<>();
    protected static final int defaultPoly = 0xD5;
    protected int poly;

    protected Crc8Checker(int poly) {
        this.poly = poly;
    }

    public static Crc8Checker getInstance() {
        return checkers.computeIfAbsent(defaultPoly, p -> new Crc8Checker(p));
    }

    public synchronized static Crc8Checker getInstance(int poly) {
        if (poly == 0) {
            return getInstance();
        } else {
            return checkers.computeIfAbsent(poly, p -> new Crc8Checker(poly));
        }
    }

    @Override
    public boolean validate(byte[] datagram, Class<?> protocolClass) {
        if (!protocolClass.isAnnotationPresent(CheckSum.class)) {
            return true;
        }

        val checkSum = protocolClass.getAnnotation(CheckSum.class);
        int byteOffset = checkSum.value();
        int start = checkSum.start();
        int length = checkSum.length();

        int actual = this.getValue(datagram, start, length);
        int expected = DecodeUtils.uInteger8Type(datagram, byteOffset);

        return actual == expected;
    }

    @Override
    public void setValue(byte[] datagram, Class<?> protocolClass) {
        if (!protocolClass.isAnnotationPresent(CheckSum.class)) {
            return;
        }

        val checkSum = protocolClass.getAnnotation(CheckSum.class);
        int byteOffset = checkSum.value();
        int start = checkSum.start();
        int length = checkSum.length();

        this.setValue(datagram, start, byteOffset, length);
    }

    @Override
    public int getSize() {
        return UInteger8Type.SIZE;
    }

    public void setValue(byte[] datagram, int byteOffset, int start, int length) {
        int value = this.getValue(datagram, start, length);

        EncodeUtils.uInteger8Type(datagram, byteOffset, value);
    }

    public int getValue(byte[] datagram, int start, int length) {
        int s = start >= 0 ? start : datagram.length + start;
        int l = length >= 0 ? length : datagram.length + length - s;

        if (s < 0) {
            throw new DecodeException(DecodeException.DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (l < 0) {
            throw new DecodeException(DecodeException.DecodeError.ILLEGAL_PARAMETER);
        } else if (s + length > datagram.length) {
            throw new DecodeException(DecodeException.DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        byte crc8 = 0;

        for (int i = 0; i < l; i ++) {
            crc8 ^= datagram[s + i];

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