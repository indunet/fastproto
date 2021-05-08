package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.ShortType;

import java.lang.annotation.Annotation;

public class ShortEncoder implements Encoder<Short> {
    @Override
    public void encode(byte[] datagram, EndianPolicy endian, Annotation dataTypeAnnotation, Short value) {
        int byteOffset = ((ShortType) dataTypeAnnotation).byteOffset();

        this.encode(datagram, byteOffset, value, endian);
    }

    public void encode(byte[] datagram, int byteOffset, short value, EndianPolicy endian) {
        if (datagram.length - ShortType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (endian == EndianPolicy.Little) {
            datagram[byteOffset] = (byte) (value & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFF);
        } else if (endian == EndianPolicy.Big) {
            datagram[byteOffset + 1] = (byte) (value & 0xFF);
            datagram[byteOffset] = (byte) (value >> 8 & 0xFF);
        }
    }
}
