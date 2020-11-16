package org.indunet.fastproto.encoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.DoubleType;
import org.indunet.fastproto.annotation.FloatType;

import java.lang.annotation.Annotation;

public class FloatEncoder implements Encoder<Float> {
    @Override
    public void encode(byte[] datagram, Endian endian, Annotation dataTypeAnnotation, Float value) {
        int byteOffset = ((FloatType) dataTypeAnnotation).byteOffset();

        if (datagram.length - FloatType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        int bits = Float.floatToIntBits(value);

        if (endian == Endian.Little) {
            datagram[byteOffset] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 24 & 0xFFL);
        } else if (endian == Endian.Big) {
            datagram[byteOffset + 3] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset] = (byte) (bits >> 24 & 0xFFL);
        }
    }
}
