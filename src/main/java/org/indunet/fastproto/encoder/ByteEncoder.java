package org.indunet.fastproto.encoder;


import org.indunet.fastproto.annotation.ByteType;

public class ByteEncoder implements Encoder {
    @Override
    public void encode(EncodeContext context) {
        byte[] datagram = context.getDatagram();
        int byteOffset = context.getDataTypeAnnotation(Byte.class);
        byte value = context.getValue(Byte.class);

        this.encode(datagram, byteOffset, value);
    }

    public void encode(byte[] datagram, int byteOffset, byte value) {
        if (byteOffset + ByteType.SIZE >= datagram.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        datagram[byteOffset] = value;
    }
}
