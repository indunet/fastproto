package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.annotation.type.StringType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * String type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.1.0
 */
public class StringDecoder implements TypeDecoder<String> {
    @Override
    public String decode(@NonNull DecodeContext context) {
        StringType type = context.getDataType(StringType.class);

        return this.decode(context.getDatagram(), type.value(), type.length(), Charset.forName(type.charsetName()));
    }

    public String decode(@NonNull byte[] datagram, int byteOffset, int length, @NonNull Charset charset) {
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
            return new String(
                    Arrays.copyOfRange(datagram, byteOffset, datagram.length - byteOffset), charset);
        } else {
            return new String(
                    Arrays.copyOfRange(datagram, byteOffset, byteOffset + length), charset);
        }
    }
}
