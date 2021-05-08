package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.IntegerType;

import java.lang.annotation.Annotation;

public class IntegerEncoder implements Encoder<Integer> {
    @Override
    public void encode(byte[] datagram, EndianPolicy endian, Annotation dataTypeAnnotation, Integer value) {
        int byteOffset = ((IntegerType) dataTypeAnnotation).byteOffset();

        this.encode(datagram, byteOffset, value, endian);
    }

    public void encode(byte[] datagram, int byteOffset, int value, EndianPolicy endian) {
        if (datagram.length - IntegerType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (endian == EndianPolicy.Little) {
            datagram[byteOffset] = (byte) (value & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFF);
            datagram[byteOffset + 2] = (byte) (value >> 16 & 0xFF);
            datagram[byteOffset + 3] = (byte) (value >> 24 & 0xFF);
        } else if (endian == EndianPolicy.Big) {
            datagram[byteOffset + 3] = (byte) (value & 0xFF);
            datagram[byteOffset + 2] = (byte) (value >> 8 & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 16 & 0xFF);
            datagram[byteOffset] = (byte) (value >> 24 & 0xFF);
        }
    }
}
