package org.indunet.fastproto.encoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.IntegerType;

import java.lang.annotation.Annotation;

public class IntegerEncoder implements Encoder<Integer> {
    @Override
    public void encode(byte[] datagram, Endian endian, Annotation dataTypeAnnotation, Integer value) {
        int byteOffset = ((IntegerType) dataTypeAnnotation).byteOffset();

        this.encode(datagram, byteOffset, value, endian);
    }

    public void encode(byte[] datagram, int byteOffset, int value, Endian endian) {
        if (datagram.length - IntegerType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (endian == Endian.Little) {
            datagram[byteOffset] = (byte) (value & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFF);
            datagram[byteOffset + 2] = (byte) (value >> 16 & 0xFF);
            datagram[byteOffset + 3] = (byte) (value >> 24 & 0xFF);
        } else if (endian == Endian.Big) {
            datagram[byteOffset + 3] = (byte) (value & 0xFF);
            datagram[byteOffset + 2] = (byte) (value >> 8 & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 16 & 0xFF);
            datagram[byteOffset] = (byte) (value >> 24 & 0xFF);
        }
    }
}
