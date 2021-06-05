package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.annotation.type.UInteger8Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * UInteger8 type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,UInteger8Type
 * @since 1.2.0
 */
public class UInteger8Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(@NonNull DecodeContext context) {
        UInteger8Type type = context.getDataType(UInteger8Type.class);

        return this.decode(context.getDatagram(), type.value());
    }

    public int decode(@NonNull final byte[] datagram, int byteOffset) {
        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + UInteger8Type.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        return datagram[byteOffset] & 0xFF;
    }
}
