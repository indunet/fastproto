package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.IntegerType;

import java.lang.annotation.Annotation;

public class IntegerDecoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(DecodeContext context) {
        IntegerType type = context.getDataType(IntegerType.class);

        return this.decode(context.getDatagram(), type.byteOffset(), context.getEndian());
    }

    public int decode(final byte[] datagram, int byteOffset, EndianPolicy endian) {
        if (datagram.length - IntegerType.SIZE < byteOffset) {
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

        return value;
    }
}
