package org.indunet.fastproto.encoder;

import org.indunet.fastproto.annotation.BinaryType;

public class BinaryEncoder implements Encoder {
    @Override
    public void encode(EncodeContext context) {
        byte[] datagram = context.getDatagram();
        int byteOffset = context.getDataTypeAnnotation(BinaryType.class).byteOffset();
        byte[] values = context.getValue(byte[].class);

        this.encode(datagram, byteOffset, values);
    }

    public void encode(byte[] datagram, int byteOffset, byte[] values) {
        if (datagram.length - values.length < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        System.arraycopy(values, 0, datagram, byteOffset, values.length);
    }
}
