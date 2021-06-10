package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.annotation.type.BooleanType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * Boolean type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,BooleanType
 * @since 1.0.0
 */
public class BooleanDecoder implements TypeDecoder<Boolean> {
    @Override
    public Boolean decode(DecodeContext context) {
        BooleanType type = context.getTypeAnnotation(BooleanType.class);

        return this.decode(context.getDatagram(), type.value(), type.bitOffset());
    }

    public boolean decode(@NonNull final byte[] datagram, int byteOffset, int bitOffset) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (bitOffset > BooleanType.MAX_BIT_OFFSET || bitOffset < BooleanType.MIN_BIT_OFFSET) {
            throw new DecodeException(DecodeError.ILLEGAL_BIT_OFFSET);
        } else if (byteOffset >= datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        } else {
            return (datagram[byteOffset] & (0x01 << bitOffset)) != 0;
        }
    }
}
