package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * Long type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.0.0
 */
public class LongDecoder implements TypeDecoder<Long> {
    @Override
    public Long decode(@NonNull DecodeContext context) {
        LongType type = context.getTypeAnnotation(LongType.class);

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy());
    }

    public long decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy endian) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + LongType.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        long value = 0;

        if (endian == EndianPolicy.LITTLE) {
            value |= (datagram[byteOffset] & 0xFF);
            value |= ((datagram[byteOffset + 1] & 0xFFL) << 8);
            value |= ((datagram[byteOffset + 2] & 0xFFL) << 16);
            value |= ((datagram[byteOffset + 3] & 0xFFL) << 24);
            value |= ((datagram[byteOffset + 4] & 0xFFL) << 32);
            value |= ((datagram[byteOffset + 5] & 0xFFL) << 40);
            value |= ((datagram[byteOffset + 6] & 0xFFL) << 48);
            value |= ((datagram[byteOffset + 7] & 0xFFL) << 56);
        } else if (endian == EndianPolicy.BIG) {
            value |= (datagram[byteOffset + 7] & 0xFF);
            value |= ((datagram[byteOffset + 6] & 0xFFL) << 8);
            value |= ((datagram[byteOffset + 5] & 0xFFL) << 16);
            value |= ((datagram[byteOffset + 4] & 0xFFL) << 24);
            value |= ((datagram[byteOffset + 3] & 0xFFL) << 32);
            value |= ((datagram[byteOffset + 2] & 0xFFL) << 40);
            value |= ((datagram[byteOffset + 1] & 0xFFL) << 48);
            value |= ((datagram[byteOffset] & 0xFFL) << 56);
        }

        return value;
    }
}
