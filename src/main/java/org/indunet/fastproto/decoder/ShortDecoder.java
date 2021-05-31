package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.ShortType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.0.0
 */
public class ShortDecoder implements TypeDecoder<Short> {
    @Override
    public Short decode(DecodeContext context) {
        ShortType type = context.getDataType(ShortType.class);

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy());
    }

    public short decode(final byte[] datagram, int byteOffset, EndianPolicy endian) {
        if (byteOffset + ShortType.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        short value = 0;

        if (endian == EndianPolicy.LITTLE) {
            value |= (datagram[byteOffset] & 0x00FF);
            value |= (datagram[byteOffset + 1] << 8);
        } else if (endian == EndianPolicy.BIG) {
            value |= (datagram[byteOffset + 1] & 0x00FF);
            value |= (datagram[byteOffset] << 8);
        }

        return value;
    }
}
