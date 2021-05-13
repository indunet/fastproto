package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.DoubleType;

public class DoubleEncoder implements TypeEncoder<Double> {
    @Override
    public void encode(EncodeContext<Double> context) {
        DoubleType type = context.getDataType(DoubleType.class);
        Double value = context.getValue();

        this.encode(context.getDatagram(), type.byteOffset(), context.getEndianPolicy(), value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy endian, double value) {
        if (datagram.length - DoubleType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        long bits = Double.doubleToLongBits(value);

        if (endian == EndianPolicy.Little) {
            datagram[byteOffset] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 24 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (bits >> 32 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (bits >> 40 & 0xFFL);
            datagram[byteOffset + 6] = (byte) (bits >> 48 & 0xFFL);
            datagram[byteOffset + 7] = (byte) (bits >> 56 & 0xFFL);
        } else if (endian == EndianPolicy.Big) {
            datagram[byteOffset + 7] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 6] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (bits >> 24 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 32 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 40 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 48 & 0xFFL);
            datagram[byteOffset] = (byte) (bits >> 56 & 0xFFL);
        }
    }
}
