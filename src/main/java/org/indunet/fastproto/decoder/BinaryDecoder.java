package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.BinaryType;

public class BinaryDecoder implements Decoder<byte[]> {
    @Override
    public byte[] decode(DecodeContext context) {
        int byteOffset = ((BinaryType) context.getDateType()).byteOffset();
        int length = ((BinaryType) context.getDateType()).length();

        return this.decode(context.getDatagram(), byteOffset, length);
    }

    public byte[] decode(final byte[] datagram, int byteOffset, int length) {
        if (datagram.length - length < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        byte[] bytes = new byte[length];

        System.arraycopy(datagram, byteOffset, bytes, 0, length);
        return bytes;
    }
}
