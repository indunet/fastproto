package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.ShortType;

import java.lang.annotation.Annotation;

public class ShortDecoder implements TypeDecoder<Short> {
    @Override
    public Short decode(DecodeContext context) {
        ShortType type = context.getDataType(ShortType.class);

        return this.decode(context.getDatagram(), type.byteOffset(), context.getEndian());
    }

    public short decode(final byte[] datagram, int byteOffset, EndianPolicy endian) {
        if (datagram.length - ShortType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        short value = 0;

        if (endian == EndianPolicy.Little) {
            value |= (datagram[byteOffset] & 0x00FF);
            value |= (datagram[byteOffset + 1] << 8);
        } else if (endian == EndianPolicy.Big) {
            value |= (datagram[byteOffset + 1] & 0x00FF);
            value |= (datagram[byteOffset] << 8);
        }

        return value;
    }
}
