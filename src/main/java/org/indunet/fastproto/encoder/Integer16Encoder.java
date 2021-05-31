package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.Integer16Type;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.text.MessageFormat;

/**
 * @author Deng Ran
 * @see TypeEncoder
 * @since 1.2.0
 */
public class Integer16Encoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        Integer16Type type = context.getDataType(Integer16Type.class);
        EndianPolicy policy = context.getEndianPolicy();
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.value(), policy, value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy policy, int value) {
        if (byteOffset + Integer16Type.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        } else if (value > Integer16Type.MAX_VALUE || value < Integer16Type.MIN_VALUE) {
            throw new EncodeException(
                    MessageFormat.format(EncodeError.EXCEEDED_TYPE_SIZE_LIMIT.getMessage(), Integer16Type.class.getName()));
        }

        if (policy == EndianPolicy.BIG) {
            datagram[byteOffset + 1] = (byte) (value & 0xFF);
            datagram[byteOffset] = (byte) (value >> 8 & 0xFF);
        } else {
            datagram[byteOffset] = (byte) (value & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFF);
        }
    }
}
