package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.UInteger8Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.2.0
 */
public class UInteger8Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(DecodeContext context) {
        UInteger8Type type = context.getDataType(UInteger8Type.class);

        return this.decode(context.getDatagram(), type.value());
    }

    public int decode(final byte[] datagram, int byteOffset) {
        if (byteOffset + UInteger8Type.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        return datagram[byteOffset] & 0xFF;
    }
}
