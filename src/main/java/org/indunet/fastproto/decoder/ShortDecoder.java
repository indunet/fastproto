package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.ShortType;

public class ShortDecoder implements Decoder<Integer> {
    @Override
    public Integer decode(DecodeContext context) {
        int byteOffset = context.getDataTypeAnnotation(ShortType.class).byteOffset();
        byte[] datagram = context.getDatagram();

        if (datagram.length - ShortType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        short value = 0;
        Endian endian = context.getEndian();

        if (endian == Endian.Little) {
            value |= (datagram[byteOffset] & 0x00FF);
            value |= (datagram[byteOffset + 1] << 8);
        } else if (endian == Endian.Big) {
            value |= (datagram[byteOffset + 1] & 0x00FF);
            value |= (datagram[byteOffset] << 8);
        }

        return (int) value;
    }
}
