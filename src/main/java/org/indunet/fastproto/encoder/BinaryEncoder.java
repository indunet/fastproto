package org.indunet.fastproto.encoder;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.annotation.type.BinaryType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * Binary type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,BinaryType
 * @since 1.0.0
 */
public class BinaryEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        val type = context.getDataType(BinaryType.class);
        val bytes = context.getValue(byte[].class);

        this.encode(context.getDatagram(), type.value(), type.length(), bytes);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, int length, @NonNull byte[] bytes) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (length < -1) {
            throw new EncodeException(EncodeError.ILLEGAL_PARAMETER);
        } else if (length == -1 && byteOffset + bytes.length > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        } else if (length != -1 && byteOffset + length > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (length == -1) {
            System.arraycopy(bytes, 0, datagram, byteOffset, bytes.length);
        } else {
            System.arraycopy(bytes, 0, datagram, byteOffset, length);
        }
    }
}
