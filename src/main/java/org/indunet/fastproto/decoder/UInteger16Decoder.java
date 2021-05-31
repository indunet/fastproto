package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.UInteger16Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.2.0
 */
public class UInteger16Decoder implements TypeDecoder<Integer> {
    @Override
    public Integer decode(DecodeContext context) {
        return null;
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
