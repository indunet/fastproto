package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.ShortType;

public class ShortDecoder implements Decoder<Short> {
    @Override
    public Short decode(DecodeContext context) {
        byte[] datagram = context.getDatagram();
        int byteOffset = context.getDataTypeAnnotation(ShortType.class).byteOffset();
        Endian endian = context.getEndian();

        return this.decode(datagram, byteOffset, endian);
    }

    public short decode(final byte[] datagram, int byteOffset, Endian endian) {
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
