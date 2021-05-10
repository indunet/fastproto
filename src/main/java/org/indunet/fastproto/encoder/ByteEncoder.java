package org.indunet.fastproto.encoder;


import org.indunet.fastproto.annotation.type.ByteType;

public class ByteEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        ByteType type = context.getDataType(ByteType.class);
        Byte value = context.getValue(Byte.class);

        this.encode(context.getDatagram(), type.byteOffset(), value);
    }

    public void encode(byte[] datagram, int byteOffset, byte value) {
        if (byteOffset + ByteType.SIZE >= datagram.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        datagram[byteOffset] = value;
    }
}
