package org.indunet.fastproto.encoder;


import org.indunet.fastproto.annotation.type.ByteType;

public class ByteEncoder implements TypeEncoder<Byte> {
    @Override
    public void encode(EncodeContext<Byte> context) {
        ByteType type = context.getDataType(ByteType.class);
        Byte value = context.getValue();

        this.encode(context.getDatagram(), type.byteOffset(), value);
    }

    public void encode(byte[] datagram, int byteOffset, byte value) {
        if (byteOffset + ByteType.SIZE >= datagram.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        datagram[byteOffset] = value;
    }
}
