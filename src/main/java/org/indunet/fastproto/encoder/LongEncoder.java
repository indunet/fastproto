package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.LongType;

public class LongEncoder implements TypeEncoder<Long> {
    @Override
    public void encode(EncodeContext<Long> context) {
        LongType type = context.getDataType(LongType.class);
        Long value = context.getValue();

        this.encode(context.getDatagram(), type.byteOffset(), context.getEndianPolicy(), value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy policy, long value) {
        if (datagram.length - LongType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (policy == EndianPolicy.Little) {
            datagram[byteOffset] = (byte) (value & 0xFFL);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (value >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (value >> 24 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (value >> 32 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (value >> 40 & 0xFFL);
            datagram[byteOffset + 6] = (byte) (value >> 48 & 0xFFL);
            datagram[byteOffset + 7] = (byte) (value >> 56 & 0xFFL);
        } else if (policy == EndianPolicy.Big) {
            datagram[byteOffset + 7] = (byte) (value & 0xFFL);
            datagram[byteOffset + 6] = (byte) (value >> 8 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (value >> 16 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (value >> 24 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (value >> 32 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (value >> 40 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (value >> 48 & 0xFFL);
            datagram[byteOffset] = (byte) (value >> 56 & 0xFFL);
        }
    }
}
