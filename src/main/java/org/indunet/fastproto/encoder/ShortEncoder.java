package org.indunet.fastproto.encoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.ShortType;

import java.lang.annotation.Annotation;

public class ShortEncoder implements Encoder<Short> {
    @Override
    public void encode(byte[] datagram, Endian endian, Annotation dataTypeAnnotation, Short value) {
        int byteOffset = ((ShortType) dataTypeAnnotation).byteOffset();

        this.encode(datagram, byteOffset, value, endian);
    }

    public void encode(byte[] datagram, int byteOffset, short value, Endian endian) {
        if (datagram.length - ShortType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (endian == Endian.Little) {
            datagram[byteOffset] = (byte) (value & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFF);
        } else if (endian == Endian.Big) {
            datagram[byteOffset + 1] = (byte) (value & 0xFF);
            datagram[byteOffset] = (byte) (value >> 8 & 0xFF);
        }
    }
}
