package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.annotation.type.Integer8Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * Integer type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,Integer8Type
 * @since 1.2.0
 */
public class Integer8Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(@NonNull DecodeContext context) {
        Integer8Type type = context.getTypeAnnotation(Integer8Type.class);

        return this.decode(context.getDatagram(), type.value());
    }

    public int decode(@NonNull final byte[] datagram, int byteOffset) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + Integer8Type.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        return datagram[byteOffset];
    }
}
