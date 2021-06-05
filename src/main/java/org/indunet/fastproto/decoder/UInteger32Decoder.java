package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * UInteger32 type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,UInteger32Type
 * @since 1.2.0
 */
public class UInteger32Decoder implements TypeDecoder<Long> {
    @Override
    public Long decode(@NonNull DecodeContext context) {
        UInteger32Type type = context.getDataType(UInteger32Type.class);
        EndianPolicy policy = context.getEndianPolicy();

        return this.decode(context.getDatagram(), type.value(), policy);
    }

    public long decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy policy) {
        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + UInteger32Type.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        long value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[byteOffset] & 0xFF);
            value |= ((datagram[byteOffset + 1] & 0xFFL) << 8);
            value |= ((datagram[byteOffset + 2] & 0xFFL) << 16);
            value |= ((datagram[byteOffset + 3] & 0xFFL) << 24);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[byteOffset + 3] & 0xFF);
            value |= ((datagram[byteOffset + 2] & 0xFFL) << 8);
            value |= ((datagram[byteOffset + 1] & 0xFFL) << 16);
            value |= ((datagram[byteOffset] & 0xFFL) << 24);
        }

        return value;
    }
}
