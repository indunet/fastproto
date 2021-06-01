package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.DoubleType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * @author Deng Ran
 * @since 1.0.0
 * @see TypeEncoder
 */
public class DoubleEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        DoubleType type = context.getDataType(DoubleType.class);
        Double value = context.getValue(Double.class);

        this.encode(context.getDatagram(), type.value(), context.getEndianPolicy(), value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy endian, double value) {
        if (byteOffset + DoubleType.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        long bits = Double.doubleToLongBits(value);

        if (endian == EndianPolicy.BIG) {
            datagram[byteOffset + 7] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 6] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (bits >> 24 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 32 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 40 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 48 & 0xFFL);
            datagram[byteOffset] = (byte) (bits >> 56 & 0xFFL);
        } else {
            datagram[byteOffset] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 24 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (bits >> 32 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (bits >> 40 & 0xFFL);
            datagram[byteOffset + 6] = (byte) (bits >> 48 & 0xFFL);
            datagram[byteOffset + 7] = (byte) (bits >> 56 & 0xFFL);
        }
    }
}
