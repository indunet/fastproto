package org.indunet.fastproto.decoder;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.annotation.type.BinaryType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * Binary type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,BinaryType
 * @since 1.0.0
 */
public class BinaryDecoder implements TypeDecoder<byte[]> {
    @Override
    public byte[] decode(@NonNull DecodeContext context) {
        val type = context.getDataType(BinaryType.class);

        return this.decode(context.getDatagram(), type.value(), type.length());
    }

    public byte[] decode(@NonNull final byte[] datagram, int byteOffset, int length) {
        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (length < -1) {
            throw new DecodeException(DecodeError.ILLEGAL_PARAMETER);
        } else if (length == -1 && byteOffset >= datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        } else if (length != -1 && byteOffset + length > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (length == -1) {
            length = datagram.length - byteOffset;
        }

        val bytes = new byte[length];

        System.arraycopy(datagram, byteOffset, bytes, 0, length);
        return bytes;
    }
}
