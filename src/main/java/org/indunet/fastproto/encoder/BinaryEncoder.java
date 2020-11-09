package org.indunet.fastproto.encoder;

import org.indunet.fastproto.annotation.BinaryType;

public class BinaryEncoder implements Encoder {
    @Override
    public void encode(EncodeContext context) {
        byte[] datagram = context.getDatagram();
        int byteOffset = context.getDataTypeAnnotation(BinaryType.class).byteOffset();
        int length = context.getDataTypeAnnotation(BinaryType.class).length();
        byte[] values = context.getValue(byte[].class);

        this.encode(datagram, byteOffset, length, values);
    }

    public void encode(byte[] datagram, int byteOffset, int length, byte[] values) {
        if (datagram.length - values.length < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        System.arraycopy(values, 0, datagram, byteOffset, length);
    }
}
