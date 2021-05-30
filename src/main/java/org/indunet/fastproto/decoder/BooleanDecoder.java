package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.BooleanType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.0.0
 */
public class BooleanDecoder implements TypeDecoder<Boolean> {
    @Override
    public Boolean decode(DecodeContext context) {
        BooleanType type = context.getDataType(BooleanType.class);

        return this.decode(context.getDatagram(), type.byteOffset(), type.bitOffset());
    }

    public boolean decode(final byte[] datagram, int byteOffset, int bitOffset) {
        if (bitOffset > BooleanType.MAX_BIT_OFFSET || bitOffset < BooleanType.MIN_BIT_OFFSET) {
            throw new DecodeException(DecodeError.ILLEGAL_BIT_OFFSET);
        } else if (byteOffset >= datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        } else {
            return (datagram[byteOffset] & (0x01 << bitOffset)) != 0;
        }
    }
}
