package org.indunet.fastproto.encoder;

import org.indunet.fastproto.annotation.type.BinaryType;

public class BinaryEncoder implements TypeEncoder<byte[]> {
    @Override
    public void encode(EncodeContext<byte[]> context) {
        BinaryType type = context.getDataType(BinaryType.class);
        byte[] bytes = context.getValue();

        this.encode(context.getDatagram(), type.byteOffset(), bytes);
    }

    public void encode(byte[] datagram, int byteOffset, byte[] bytes) {
        if (datagram.length - bytes.length < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        System.arraycopy(bytes, 0, datagram, byteOffset, bytes.length);
    }
}
