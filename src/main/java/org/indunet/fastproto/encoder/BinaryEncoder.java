package org.indunet.fastproto.encoder;

import org.indunet.fastproto.annotation.type.BinaryType;

public class BinaryEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        BinaryType type = context.getDataType(BinaryType.class);
        byte[] bytes = context.getValue(byte[].class);

        this.encode(context.getDatagram(), type.byteOffset(), bytes);
    }

    public void encode(byte[] datagram, int byteOffset, byte[] bytes) {
        if (datagram.length - bytes.length < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        System.arraycopy(bytes, 0, datagram, byteOffset, bytes.length);
    }
}
