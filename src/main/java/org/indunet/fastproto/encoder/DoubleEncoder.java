package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.DoubleType;

import java.lang.annotation.Annotation;

public class DoubleEncoder implements Encoder<Double> {
    @Override
    public void encode(byte[] datagram, EndianPolicy endian, Annotation dataTypeAnnotation, Double value) {
        int byteOffset = ((DoubleType) dataTypeAnnotation).byteOffset();

        this.encode(datagram, byteOffset, value, endian);
    }

    public void encode(byte[] datagram, int byteOffset, double value, EndianPolicy endian) {
        if (datagram.length - DoubleType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        long bits = Double.doubleToLongBits(value);

        if (endian == EndianPolicy.Little) {
            datagram[byteOffset] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 24 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (bits >> 32 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (bits >> 40 & 0xFFL);
            datagram[byteOffset + 6] = (byte) (bits >> 48 & 0xFFL);
            datagram[byteOffset + 7] = (byte) (bits >> 56 & 0xFFL);
        } else if (endian == EndianPolicy.Big) {
            datagram[byteOffset + 7] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 6] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (bits >> 24 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 32 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 40 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 48 & 0xFFL);
            datagram[byteOffset] = (byte) (bits >> 56 & 0xFFL);
        }
    }
}
