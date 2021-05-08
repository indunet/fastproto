package org.indunet.fastproto.encoder;

import org.indunet.fastproto.annotation.type.BinaryType;

public class BinaryEncoder implements Encoder<byte[]> {
    @Override
    public void encode(EncodeContext context, byte[] bytes) {
        int byteOffset = ((BinaryType) context.getDataType()).byteOffset();

        this.encode(context.getDatagram(), byteOffset, bytes);
    }

    public void encode(byte[] datagram, int byteOffset, byte[] bytes) {
        if (datagram.length - bytes.length < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        System.arraycopy(bytes, 0, datagram, byteOffset, bytes.length);
    }
}
