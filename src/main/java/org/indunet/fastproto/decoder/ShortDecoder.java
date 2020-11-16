package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.ShortType;

import java.lang.annotation.Annotation;

public class ShortDecoder implements Decoder<Short> {
    @Override
    public Short decode(final byte[] datagram, Endian endian, Annotation dataTypeAnnotation) {
        int byteOffset = ((ShortType) dataTypeAnnotation).byteOffset();

        if (datagram.length - ShortType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        short value = 0;

        if (endian == Endian.Little) {
            value |= (datagram[byteOffset] & 0x00FF);
            value |= (datagram[byteOffset + 1] << 8);
        } else if (endian == Endian.Big) {
            value |= (datagram[byteOffset + 1] & 0x00FF);
            value |= (datagram[byteOffset] << 8);
        }

        return value;
    }
}
