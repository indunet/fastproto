package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.StringType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.1.0
 */
public class StringDecoder implements TypeDecoder<String> {
    @Override
    public String decode(DecodeContext context) {
        StringType type = context.getDataType(StringType.class);

        return this.decode(context.getDatagram(), type.byteOffset(), type.length(), Charset.forName(type.charsetName()));
    }

    public String decode(byte[] datagram, int byteOffset, int length, Charset charset) {
        if (length == -1) {
            return new String(
                    Arrays.copyOfRange(datagram, byteOffset, datagram.length - byteOffset), charset);
        } else if (byteOffset + length > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        } else {
            return new String(
                    Arrays.copyOfRange(datagram, byteOffset, byteOffset + length), charset);
        }
    }
}
