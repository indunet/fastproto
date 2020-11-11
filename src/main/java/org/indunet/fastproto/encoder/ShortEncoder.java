package org.indunet.fastproto.encoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.ShortType;

public class ShortEncoder implements Encoder {
    @Override
    public void encode(EncodeContext context) {
        int byteOffset = context.getDataTypeAnnotation(ShortType.class).byteOffset();
        Endian endian = context.getEndian();
        byte[] datagram = context.getDatagram();
        short value = context.getValue(Short.class);

        this.encode(datagram, byteOffset, value, endian);
    }

    public void encode(byte[] datagram, int byteOffset, short value, Endian endian) {
        if (datagram.length - ShortType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (endian == Endian.Little) {
            datagram[byteOffset] = (byte) (value & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFF);
        } else if (endian == Endian.Big) {
            datagram[byteOffset + 1] = (byte) (value & 0xFF);
            datagram[byteOffset] = (byte) (value >> 8 & 0xFF);
        }
    }
}
