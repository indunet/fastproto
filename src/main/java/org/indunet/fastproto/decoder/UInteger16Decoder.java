package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.UInteger16Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * UInteger16 type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,UInteger16Type
 * @since 1.2.0
 */
public class UInteger16Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(@NonNull DecodeContext context) {
        UInteger16Type type = context.getTypeAnnotation(UInteger16Type.class);
        EndianPolicy policy = context.getEndianPolicy();

        return this.decode(context.getDatagram(), type.value(), policy);
    }

    public int decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy policy) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + UInteger16Type.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (policy == EndianPolicy.BIG) {
            return (datagram[byteOffset] & 0xFF) * 256 + (datagram[byteOffset + 1] & 0xFF);
        } else {
            return (datagram[byteOffset + 1] & 0xFF) * 256 + (datagram[byteOffset] & 0xFF);
        }
    }
}
