package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.Integer16Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * Integer type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.2.0
 */
public class Integer16Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(DecodeContext context) {
        Integer16Type type = context.getDataType(Integer16Type.class);
        EndianPolicy policy = context.getEndianPolicy();

        return this.decode(context.getDatagram(), type.value(), policy);
    }

    public int decode(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        if (byteOffset + Integer16Type.SIZE > datagram.length) {
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
