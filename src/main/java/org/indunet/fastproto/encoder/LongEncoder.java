package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.LongType;

import java.lang.annotation.Annotation;

public class LongEncoder implements Encoder<Long> {
    @Override
    public void encode(byte[] datagram, EndianPolicy endian, Annotation dataTypeAnnotation, Long value) {
        int byteOffset = ((LongType) dataTypeAnnotation).byteOffset();

        this.encode(datagram, byteOffset, value, endian);
    }

    public void encode(byte[] datagram, int byteOffset, long value, EndianPolicy endian) {
        if (datagram.length - LongType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (endian == EndianPolicy.Little) {
            datagram[byteOffset] = (byte) (value & 0xFFL);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (value >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (value >> 24 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (value >> 32 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (value >> 40 & 0xFFL);
            datagram[byteOffset + 6] = (byte) (value >> 48 & 0xFFL);
            datagram[byteOffset + 7] = (byte) (value >> 56 & 0xFFL);
        } else if (endian == EndianPolicy.Big) {
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
