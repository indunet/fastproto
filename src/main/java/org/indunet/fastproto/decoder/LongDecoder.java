package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.LongType;

import java.lang.annotation.Annotation;

public class LongDecoder implements Decoder<Long> {
    @Override
    public Long decode(final byte[] datagram, EndianPolicy endian, Annotation dataTypeAnnotation) {
        int byteOffset = ((LongType) dataTypeAnnotation).byteOffset();

        return this.decode(datagram, byteOffset, endian);
    }

    public long decode(final byte[] datagram, int byteOffset, EndianPolicy endian) {
        if (datagram.length - LongType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        long value = 0;

        if (endian == EndianPolicy.Little) {
            value |= (datagram[byteOffset] & 0xFF);
            value |= ((long) (datagram[byteOffset + 1] & 0xFFL) << 8);
            value |= ((long) (datagram[byteOffset + 2] & 0xFFL) << 16);
            value |= ((long) (datagram[byteOffset + 3] & 0xFFL) << 24);
            value |= ((long) (datagram[byteOffset + 4] & 0xFFL) << 32);
            value |= ((long) (datagram[byteOffset + 5] & 0xFFL) << 40);
            value |= ((long) (datagram[byteOffset + 6] & 0xFFL) << 48);
            value |= ((long) (datagram[byteOffset + 7] & 0xFFL) << 56);
        } else if (endian == EndianPolicy.Big) {
            value |= (datagram[byteOffset + 7] & 0xFF);
            value |= ((long) (datagram[byteOffset + 6] & 0xFFL) << 8);
            value |= ((long) (datagram[byteOffset + 5] & 0xFFL) << 16);
            value |= ((long) (datagram[byteOffset + 4] & 0xFFL) << 24);
            value |= ((long) (datagram[byteOffset + 3] & 0xFFL) << 32);
            value |= ((long) (datagram[byteOffset + 2] & 0xFFL) << 40);
            value |= ((long) (datagram[byteOffset + 1] & 0xFFL) << 48);
            value |= ((long) (datagram[byteOffset] & 0xFFL) << 56);
        }

        return value;
    }
}
