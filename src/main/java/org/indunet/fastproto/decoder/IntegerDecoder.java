package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.IntegerType;

public class IntegerDecoder implements Decoder<Integer> {
    @Override
    public Integer decode(DecodeContext context) {
        byte[] datagram = context.getDatagram();
        int byteOffset = context.getDataTypeAnnotation(IntegerType.class).byteOffset();
        Endian endian = context.getEndian();

        return this.decode(datagram, byteOffset, endian);
    }

    public int decode(final byte[] datagram, int byteOffset, Endian endian) {
        if (datagram.length - Integer.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        int value = 0;

        if (endian == Endian.Little) {
            value |= (datagram[byteOffset] & 0xFF);
            value |= ((datagram[byteOffset + 1] & 0xFF) << 8);
            value |= ((datagram[byteOffset + 2] & 0xFF) << 16);
            value |= ((datagram[byteOffset + 3] & 0xFF) << 24);
        } else if (endian == Endian.Big) {
            value |= (datagram[byteOffset + 3] & 0xFF);
            value |= ((datagram[byteOffset + 2] & 0xFF) << 8);
            value |= ((datagram[byteOffset + 1] & 0xFF) << 16);
            value |= ((datagram[byteOffset] & 0xFF) << 24);
        }

        return value;
    }
}
