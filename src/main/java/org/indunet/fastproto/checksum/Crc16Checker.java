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

package org.indunet.fastproto.checksum;

import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.EnableChecksum;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.type.UInteger16Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.OutOfBoundsException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.ReverseUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Deng Ran
 * @since 1.6.3
 */
public class Crc16Checker implements Checker {
    protected final static int defaultPoly = 0xA001;
    protected final static Map<Integer, Crc16Checker> checkers = new ConcurrentHashMap<>();
    protected int poly;

    protected Crc16Checker(int poly) {
        this.poly = poly;
    }

    public static Crc16Checker getInstance() {
        return checkers.computeIfAbsent(defaultPoly, p -> new Crc16Checker(p));
    }

    public static synchronized Crc16Checker getInstance(int poly) {
        if (poly == 0) {
            return getInstance();
        } else {
            return checkers.computeIfAbsent(poly, p -> new Crc16Checker(p));
        }
    }

    @Override
    public boolean validate(byte[] datagram, Class<?> protocolClass) {
        if (!protocolClass.isAnnotationPresent(EnableChecksum.class)) {
            return true;
        }

        val checkSum = protocolClass.getAnnotation(EnableChecksum.class);
        int byteOffset = checkSum.value();
        int start = checkSum.start();
        int length = checkSum.length();
        EndianPolicy policy;

        if (checkSum.endianPolicy().length != 0) {
            policy = checkSum.endianPolicy()[0];
        } else if (protocolClass.isAnnotationPresent(Endian.class)) {
            policy = protocolClass.getAnnotation(Endian.class).value();
        } else {
            policy = EndianPolicy.LITTLE;
        }

        int actual = this.getValue(datagram, start, length);
        int expected = CodecUtils.uinteger16Type(datagram, ReverseUtils.offset(datagram.length, byteOffset), policy);

        return actual == expected;
    }

    @Override
    public void setValue(byte[] datagram, Class<?> protocolClass) {
        if (!protocolClass.isAnnotationPresent(EnableChecksum.class)) {
            return;
        }

        val checkSum = protocolClass.getAnnotation(EnableChecksum.class);
        int byteOffset = checkSum.value();
        int start = checkSum.start();
        int length = checkSum.length();
        EndianPolicy policy;

        if (checkSum.endianPolicy().length != 0) {
            policy = checkSum.endianPolicy()[0];
        } else if (protocolClass.isAnnotationPresent(Endian.class)) {
            policy = protocolClass.getAnnotation(Endian.class).value();
        } else {
            policy = EndianPolicy.LITTLE;
        }

        this.setValue(datagram, byteOffset, start, length, policy);
    }

    @Override
    public int getSize() {
        return UInteger16Type.SIZE;
    }

    public int getValue(byte[] datagram, int start, int length) {
        int s = ReverseUtils.offset(datagram.length, start);
        int l = ReverseUtils.length(datagram.length, start, length);

        if (s < 0) {
            throw new DecodingException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (l <= 0) {
            throw new DecodingException(CodecError.ILLEGAL_PARAMETER);
        } else if (s + length > datagram.length) {
            throw new OutOfBoundsException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        int crc16 = 0xFFFF;

        for (int i = 0; i < l; i++) {
            crc16 ^= ((int) datagram[s + i] & 0xFF);

            for (int j = 0; j < 8; j++) {
                if ((crc16 & 0x0001) == 1) {
                    crc16 >>>= 1;
                    crc16 ^= poly;
                } else {
                    crc16 >>>= 1;
                }
            }
        }

        return crc16;
    }

    public void setValue(byte[] datagram, int byteOffset, int start, int length, EndianPolicy policy) {
        int value = this.getValue(datagram, start, length);

        CodecUtils.uinteger16Type(datagram, ReverseUtils.offset(datagram.length, byteOffset), policy, value);
    }
}
