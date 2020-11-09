package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.BinaryType;

public class BinaryDecoder implements Decoder<byte[]> {
    @Override
    public byte[] decode(DecodeContext context) {
        byte[] datagram = context.getDatagram();
        int byteOffset = context.getDataTypeAnnotation(BinaryType.class).byteOffset();
        int length = context.getDataTypeAnnotation(BinaryType.class).length();

        return this.decode(datagram, byteOffset, length);
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
