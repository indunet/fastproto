package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.Integer8Type;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.text.MessageFormat;

/**
 * @author Deng Ran
 * @see TypeEncoder
 * @since 1.2.0
 */
public class Integer8Encoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        Integer8Type type = context.getDataType(Integer8Type.class);
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.value(), value);
    }

    public void encode(byte[] datagram, int byteOffset, int value) {
        if (byteOffset + Integer8Type.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        } else if (value > Integer8Type.MAX_VALUE || value < Integer8Type.MIN_VALUE) {
            throw new EncodeException(
                    MessageFormat.format(EncodeError.EXCEEDED_TYPE_SIZE_LIMIT.getMessage(), Integer8Type.class.getName()));
        }

        datagram[byteOffset] = (byte) value;
    }
}
