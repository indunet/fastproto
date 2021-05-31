package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.BinaryType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.0.0
 */
public class BinaryDecoder implements TypeDecoder<byte[]> {
    @Override
    public byte[] decode(DecodeContext context) {
        BinaryType type = context.getDataType(BinaryType.class);

        return this.decode(context.getDatagram(), type.byteOffset(), type.length());
    }

    public byte[] decode(final byte[] datagram, int byteOffset, int length) {
        if (byteOffset + length > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE, new ArrayIndexOutOfBoundsException());
        }

        byte[] bytes = new byte[length];

        System.arraycopy(datagram, byteOffset, bytes, 0, length);
        return bytes;
    }
}
