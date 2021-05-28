package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.DoubleType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

public class DoubleDecoder implements TypeDecoder<Double> {
    @Override
    public Double decode(DecodeContext context) {
        DoubleType type = context.getDataType(DoubleType.class);

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy());
    }

    public double decode(final byte[] datagram, int byteOffset, EndianPolicy endian) {
        if (datagram.length - DoubleType.SIZE < byteOffset) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        long value = 0;

        if (endian == EndianPolicy.LITTLE) {
            value |= (datagram[byteOffset] & 0xFFL);
            value |= ((datagram[byteOffset + 1] & 0xFFL) << 8);
            value |= ((datagram[byteOffset + 2] & 0xFFL) << 16);
            value |= ((datagram[byteOffset + 3] & 0xFFL) << 24);
            value |= ((datagram[byteOffset + 4] & 0xFFL) << 32);
            value |= ((datagram[byteOffset + 5] & 0xFFL) << 40);
            value |= ((datagram[byteOffset + 6] & 0xFFL) << 48);
            value |= ((datagram[byteOffset + 7] & 0xFFL) << 56);
        } else if (endian == EndianPolicy.BIG) {
            value |= (datagram[byteOffset + 7] & 0xFFL);
            value |= ((datagram[byteOffset + 6] & 0xFFL) << 8);
            value |= ((datagram[byteOffset + 5] & 0xFFL) << 16);
            value |= ((datagram[byteOffset + 4] & 0xFFL) << 24);
            value |= ((datagram[byteOffset + 3] & 0xFFL) << 32);
            value |= ((datagram[byteOffset + 2] & 0xFFL) << 40);
            value |= ((datagram[byteOffset + 1] & 0xFFL) << 48);
            value |= ((datagram[byteOffset] & 0xFFL) << 56);
        }

        return Double.longBitsToDouble(value);
    }
}
