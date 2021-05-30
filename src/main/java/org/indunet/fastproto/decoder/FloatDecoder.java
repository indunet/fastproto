package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.FloatType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.0.0
 */
public class FloatDecoder implements TypeDecoder<Float> {
    @Override
    public Float decode(DecodeContext context) {
        FloatType type = context.getDataType(FloatType.class);

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy());
    }

    public float decode(final byte[] datagram, int byteOffset, EndianPolicy endian) {
        if (datagram.length - FloatType.SIZE < byteOffset) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        int value = 0;


        if (endian == EndianPolicy.LITTLE) {
            value |= (datagram[byteOffset] & 0xFF);
            value |= ((datagram[byteOffset + 1] & 0xFF) << 8);
            value |= ((datagram[byteOffset + 2] & 0xFF) << 16);
            value |= ((datagram[byteOffset + 3] & 0xFF) << 24);
        } else if (endian == EndianPolicy.BIG) {
            value |= (datagram[byteOffset + 3] & 0xFF);
            value |= ((datagram[byteOffset + 2] & 0xFF) << 8);
            value |= ((datagram[byteOffset + 1] & 0xFF) << 16);
            value |= ((datagram[byteOffset] & 0xFF) << 24);
        }

        return Float.intBitsToFloat(value);
    }
}
