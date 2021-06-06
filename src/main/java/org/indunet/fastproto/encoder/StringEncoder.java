package org.indunet.fastproto.encoder;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.annotation.type.StringType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.nio.charset.Charset;

/**
 * String type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,StringType
 * @since 1.1.0
 */
public class StringEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        val dataType = context.getDataType(StringType.class);
        val value = context.getValue(String.class);

        this.encode(context.getDatagram(), dataType.value(), dataType.length(), Charset.defaultCharset(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, int length, @NonNull Charset set, @NonNull String value) {
        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (length < -1) {
            throw new EncodeException(EncodeError.ILLEGAL_PARAMETER);
        }

        val bytes = value.getBytes(set);

        if (length == -1 && byteOffset + bytes.length > datagram.length ) {
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