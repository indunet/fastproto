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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.CheckSum;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;
import org.indunet.fastproto.decoder.DecodeUtils;
import org.indunet.fastproto.encoder.EncodeUtils;

import java.util.zip.CRC32;

/**
 * @author Deng Ran
 * @see Checker
 * @since 1.6.0
 */
public class Crc32Checker implements Checker {
    protected final static Crc32Checker checker = new Crc32Checker();

    public static Crc32Checker getInstance() {
        return checker;
    }

    @Override
    public boolean validate(byte[] datagram, Class<?> protocolClass) {
        if (!protocolClass.isAnnotationPresent(CheckSum.class)) {
            return true;
        }

        val checkSum = protocolClass.getAnnotation(CheckSum.class);
        int byteOffset = checkSum.byteOffset();
        int length = checkSum.length();

        EndianPolicy policy;

        if (checkSum.endianPolicy().length != 0) {
            policy = checkSum.endianPolicy()[0];
        } else if (protocolClass.isAnnotationPresent(Endian.class)) {
            policy = protocolClass.getAnnotation(Endian.class).value();
        } else {
            policy = EndianPolicy.LITTLE;
        }

        long actual = this.getValue(datagram, byteOffset, length);

        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;
        int l = length >= 0 ? length : datagram.length + length - bo;
        long expected = DecodeUtils.uInteger32Type(datagram, bo + l, policy);

        return actual == expected;
    }

    public long getValue(byte[] datagram, int byteOffset, int length) {
        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;
        int l = length >= 0 ? length : datagram.length + length - bo;

        if (bo < 0) {
            throw new DecodeException(DecodeException.DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (l < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_PARAMETER);
        } else if (bo + length > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        CRC32 crc32 = new CRC32();
        crc32.update(datagram, bo, l);

        return crc32.getValue();
    }

    @Override
    public void setValue(byte[] datagram, Class<?> protocolClass) {
        if (!protocolClass.isAnnotationPresent(CheckSum.class)) {
            return;
        }

        val checkSum = protocolClass.getAnnotation(CheckSum.class);
        int byteOffset = checkSum.byteOffset();
        int length = checkSum.length();
        EndianPolicy policy;

        if (checkSum.endianPolicy().length != 0) {
            policy = checkSum.endianPolicy()[0];
        } else if (protocolClass.isAnnotationPresent(Endian.class)) {
            policy = protocolClass.getAnnotation(Endian.class).value();
        } else {
            policy = EndianPolicy.LITTLE;
        }

        this.setValue(datagram, byteOffset, length, policy);
    }

    @Override
    public int getSize() {
        return UInteger32Type.SIZE;
    }

    public void setValue(byte[] datagram, int byteOffset, int length, EndianPolicy policy) {
        int bo = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;
        int l = length >= 0 ? length : datagram.length + length - bo;
        long value = this.getValue(datagram, byteOffset, length);

        EncodeUtils.uInteger32Type(datagram, bo + l, policy, value);
    }
}
