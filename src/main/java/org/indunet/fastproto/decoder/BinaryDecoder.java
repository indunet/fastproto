package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.BinaryType;

public class BinaryDecoder implements Decoder<byte[]> {
    @Override
    public byte[] decode(DecodeContext context) {
        int byteOffset = ((BinaryType) context.getDataTypeAnnotation()).byteOffset();
        int length = ((BinaryType) context.getDataTypeAnnotation()).length();
        byte[] datagram = context.getDatagram();

        if (datagram.length - length < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            byte[] bytes = new byte[length];

            System.arraycopy(datagram, byteOffset, bytes, 0, length);
            return bytes;
        }
    }
}
