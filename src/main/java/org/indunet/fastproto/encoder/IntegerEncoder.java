package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.IntegerType;

public class IntegerEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        IntegerType type = context.getDataType(IntegerType.class);
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.byteOffset(), context.getEndianPolicy(), value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy policy, int value) {
        if (datagram.length - IntegerType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (policy == EndianPolicy.Little) {
            datagram[byteOffset] = (byte) (value & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFF);
            datagram[byteOffset + 2] = (byte) (value >> 16 & 0xFF);
            datagram[byteOffset + 3] = (byte) (value >> 24 & 0xFF);
        } else if (policy == EndianPolicy.Big) {
            datagram[byteOffset + 3] = (byte) (value & 0xFF);
            datagram[byteOffset + 2] = (byte) (value >> 8 & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 16 & 0xFF);
            datagram[byteOffset] = (byte) (value >> 24 & 0xFF);
        }
    }
}
