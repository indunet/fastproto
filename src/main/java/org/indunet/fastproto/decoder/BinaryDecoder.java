package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.BinaryType;

public class BinaryDecoder implements TypeDecoder<byte[]> {
    @Override
    public byte[] decode(DecodeContext context) {
        BinaryType type = context.getDataType(BinaryType.class);

        return this.decode(context.getDatagram(), type.byteOffset(), type.length());
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
