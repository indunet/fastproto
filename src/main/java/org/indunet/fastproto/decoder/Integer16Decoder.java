package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.Integer16Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * Integer type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,Integer16Type
 * @since 1.2.0
 */
public class Integer16Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(@NonNull DecodeContext context) {
        Integer16Type type = context.getDataType(Integer16Type.class);
        EndianPolicy policy = context.getEndianPolicy();

        return this.decode(context.getDatagram(), type.value(), policy);
    }

    public int decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy policy) {
        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + Integer16Type.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        short value = 0;

        if (policy == EndianPolicy.BIG) {
            value |= (datagram[byteOffset + 1] & 0x00FF);
            value |= (datagram[byteOffset] << 8);
        } else {
            value |= (datagram[byteOffset] & 0x00FF);
            value |= (datagram[byteOffset + 1] << 8);
        }

        return value;
    }
}
