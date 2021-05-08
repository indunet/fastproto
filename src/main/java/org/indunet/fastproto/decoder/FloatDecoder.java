package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.FloatType;

import java.lang.annotation.Annotation;

public class FloatDecoder implements Decoder<Float> {
    @Override
    public Float decode(final byte[] datagram, EndianPolicy endian, Annotation dataTypeAnnotation) {
        int byteOffset = ((FloatType) dataTypeAnnotation).byteOffset();

        return this.decode(datagram, byteOffset, endian);
    }

    public float decode(final byte[] datagram, int byteOffset, EndianPolicy endian) {
        if (datagram.length - FloatType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        int value = 0;


        if (endian == EndianPolicy.Little) {
            value |= (datagram[byteOffset] & 0xFF);
            value |= ((datagram[byteOffset + 1] & 0xFF) << 8);
            value |= ((datagram[byteOffset + 2] & 0xFF) << 16);
            value |= ((datagram[byteOffset + 3] & 0xFF) << 24);
        } else if (endian == EndianPolicy.Big) {
            value |= (datagram[byteOffset + 3] & 0xFF);
            value |= ((datagram[byteOffset + 2] & 0xFF) << 8);
            value |= ((datagram[byteOffset + 1] & 0xFF) << 16);
            value |= ((datagram[byteOffset] & 0xFF) << 24);
        }

        return Float.intBitsToFloat(value);
    }
}
