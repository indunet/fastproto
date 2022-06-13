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

package org.indunet.fastproto.checksum;

import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.EnableChecksum;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.type.UInt32Type;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.OutOfBoundsException;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.util.ReverseUtils;

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
        if (!protocolClass.isAnnotationPresent(EnableChecksum.class)) {
            return true;
        }

        val checksum = protocolClass.getAnnotation(EnableChecksum.class);
        EndianPolicy policy;

        if (checksum.endianPolicy().length != 0) {
            policy = checksum.endianPolicy()[0];
        } else if (protocolClass.isAnnotationPresent(Endian.class)) {
            policy = protocolClass.getAnnotation(Endian.class).value();
        } else {
            policy = EndianPolicy.LITTLE;
        }

        long actual = this.getValue(datagram, checksum.start(), checksum.length());
        long expected = CodecUtils.uint32Type(datagram, ReverseUtils.offset(datagram.length, checksum.value()), policy);

        return actual == expected;
    }

    public long getValue(byte[] datagram, int start, int length) {
        int s = ReverseUtils.offset(datagram.length, start);
        int l = ReverseUtils.length(datagram.length, start, length);

        if (s < 0) {
            throw new DecodingException(CodecError.ILLEGAL_BYTE_OFFSET);
        } else if (l <= 0) {
            throw new DecodingException(CodecError.ILLEGAL_PARAMETER);
        } else if (s + length > datagram.length) {
            throw new OutOfBoundsException(CodecError.EXCEEDED_DATAGRAM_SIZE);
        }

        CRC32 crc32 = new CRC32();
        crc32.update(datagram, s, l);

        return crc32.getValue();
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
        return UInt32Type.SIZE;
    }

    public void setValue(byte[] datagram, int byteOffset, int start, int length, EndianPolicy policy) {
        long value = this.getValue(datagram, start, length);

        CodecUtils.uint32Type(datagram, ReverseUtils.offset(datagram.length, byteOffset), policy, value);
    }
}
