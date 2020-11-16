package org.indunet.fastproto.encoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.IntegerType;
import org.indunet.fastproto.annotation.LongType;

import java.lang.annotation.Annotation;

public class LongEncoder implements Encoder<Long> {
    @Override
    public void encode(byte[] datagram, Endian endian, Annotation dataTypeAnnotation, Long value) {
        int byteOffset = ((LongType) dataTypeAnnotation).byteOffset();

        if (datagram.length - LongType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (endian == Endian.Little) {
            datagram[byteOffset] = (byte) (value & 0xFFL);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (value >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (value >> 24 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (value >> 32 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (value >> 40 & 0xFFL);
            datagram[byteOffset + 6] = (byte) (value >> 48 & 0xFFL);
            datagram[byteOffset + 7] = (byte) (value >> 56 & 0xFFL);
        } else if (endian == Endian.Big) {
            datagram[byteOffset + 7] = (byte) (value & 0xFFL);
            datagram[byteOffset + 6] = (byte) (value >> 8 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (value >> 16 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (value >> 24 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (value >> 32 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (value >> 40 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (value >> 48 & 0xFFL);
            datagram[byteOffset] = (byte) (value >> 56 & 0xFFL);
        }
    }
}
