package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * Integer type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,IntegerType
 * @since 1.0.0
 */
public class IntegerDecoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(@NonNull DecodeContext context) {
        IntegerType type = context.getTypeAnnotation(IntegerType.class);

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy());
    }

    public int decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy endian) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + IntegerType.SIZE > datagram.length) {
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

        return value;
    }
}
