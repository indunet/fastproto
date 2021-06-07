package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.FloatType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * Float type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,FloatType
 * @since 1.0.0
 */
public class FloatEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        FloatType type = context.getDataType(FloatType.class);
        Float value = context.getValue(Float.class);

        this.encode(context.getDatagram(), type.value(), context.getEndianPolicy(), value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy endian, float value) {
        if (byteOffset + FloatType.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        int bits = Float.floatToIntBits(value);

        if (endian == EndianPolicy.LITTLE) {
            datagram[byteOffset] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (bits >> 24 & 0xFFL);
        } else if (endian == EndianPolicy.BIG) {
            datagram[byteOffset + 3] = (byte) (bits & 0xFFL);
            datagram[byteOffset + 2] = (byte) (bits >> 8 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (bits >> 16 & 0xFFL);
            datagram[byteOffset] = (byte) (bits >> 24 & 0xFFL);
        }
    }
}
