package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.DoubleType;

import java.lang.annotation.Annotation;

public class DoubleDecoder implements Decoder<Double> {
    @Override
    public Double decode(final byte[] datagram, Endian endian, Annotation dataTypeAnnotation) {
        int byteOffset = ((DoubleType) dataTypeAnnotation).byteOffset();

        return this.decode(datagram, byteOffset, endian);
    }

    public double decode(final byte[] datagram, int byteOffset, Endian endian) {
        if (datagram.length - DoubleType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        long value = 0;

        if (endian == Endian.Little) {
            value |= (datagram[byteOffset] & 0xFFL);
            value |= ((datagram[byteOffset + 1] & 0xFFL) << 8);
            value |= ((datagram[byteOffset + 2] & 0xFFL) << 16);
            value |= ((datagram[byteOffset + 3] & 0xFFL) << 24);
            value |= ((datagram[byteOffset + 4] & 0xFFL) << 32);
            value |= ((datagram[byteOffset + 5] & 0xFFL) << 40);
            value |= ((datagram[byteOffset + 6] & 0xFFL) << 48);
            value |= ((datagram[byteOffset + 7] & 0xFFL) << 56);
        } else if (endian == Endian.Big) {
            value |= (datagram[byteOffset + 7] & 0xFFL);
            value |= ((datagram[byteOffset + 6] & 0xFFL) << 8);
            value |= ((datagram[byteOffset + 5] & 0xFFL) << 16);
            value |= ((datagram[byteOffset + 4] & 0xFFL) << 24);
            value |= ((datagram[byteOffset + 3] & 0xFFL) << 32);
            value |= ((datagram[byteOffset + 2] & 0xFFL) << 40);
            value |= ((datagram[byteOffset + 1] & 0xFFL) << 48);
            value |= ((datagram[byteOffset] & 0xFFL) << 56);
        }

        return Double.longBitsToDouble(value);
    }
}
