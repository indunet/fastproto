package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.UInteger16Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * UInteger16 type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.2.0
 */
public class UInteger16Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(DecodeContext context) {
        UInteger16Type type = context.getDataType(UInteger16Type.class);
        EndianPolicy policy = context.getEndianPolicy();

        return this.decode(context.getDatagram(), type.value(), policy);
    }

    public int decode(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        if (byteOffset + UInteger16Type.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (policy == EndianPolicy.BIG) {
            return (datagram[byteOffset] & 0xFF) * 256 + (datagram[byteOffset + 1] & 0xFF);
        } else {
            return (datagram[byteOffset + 1] & 0xFF) * 256 + (datagram[byteOffset] & 0xFF);
        }
    }
}
