package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.FloatType;

public class FloatEncoder implements TypeEncoder<Float> {
    @Override
    public void encode(EncodeContext<Float> context) {
        FloatType type = context.getDataType(FloatType.class);
        Float value = context.getValue();

        this.encode(context.getDatagram(), type.byteOffset(), context.getEndianPolicy(), value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy endian, float value) {
        if (datagram.length - FloatType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        int bits = Float.floatToIntBits(value);

        if (endian == EndianPolicy.Little) {
            datagram[byteOffset] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 24 & 0xFFL);
        } else if (endian == EndianPolicy.Big) {
            datagram[byteOffset + 3] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset] = (byte) (bits >> 24 & 0xFFL);
        }
    }
}
