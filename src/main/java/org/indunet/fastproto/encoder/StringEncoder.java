package org.indunet.fastproto.encoder;

import org.indunet.fastproto.annotation.type.StringType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.nio.charset.Charset;

/**
 * String type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder
 * @since 1.1.0
 */
public class StringEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        StringType dataType = context.getDataType(StringType.class);
        String value = context.getValue(String.class);

        this.encode(context.getDatagram(), dataType.value(), dataType.length(), Charset.defaultCharset(), value);
    }

    // TODO, throw exception.
    public void encode(byte[] datagram, int byteOffset, int length, Charset set, String value) {
        byte[] bytes = value.getBytes(set);

        if (length == -1) {
            System.arraycopy(bytes, 0, datagram, byteOffset, bytes.length);
        } else if (byteOffset + length > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        } else {
            System.arraycopy(bytes, 0, datagram, byteOffset, length);
        }
    }
}
