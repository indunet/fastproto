package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.Integer8Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * Integer type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.2.0
 */
public class Integer8Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(DecodeContext context) {
        Integer8Type type = context.getDataType(Integer8Type.class);

        return this.decode(context.getDatagram(), type.value());
    }

    public int decode(final byte[] datagram, int byteOffset) {
        if (byteOffset + Integer8Type.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        return datagram[byteOffset];
    }
}
