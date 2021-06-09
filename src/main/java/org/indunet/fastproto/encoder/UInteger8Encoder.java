package org.indunet.fastproto.encoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.UInteger8Type;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.text.MessageFormat;

/**
 * UInteger8 type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,UInteger8Type
 * @since 1.2.0
 */
public class UInteger8Encoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        UInteger8Type type = context.getDataType(UInteger8Type.class);
        EndianPolicy policy = context.getEndianPolicy();
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.value(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, int value) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + UInteger8Type.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        } else if (value > UInteger8Type.MAX_VALUE || value < UInteger8Type.MIN_VALUE) {
            throw new EncodeException(
                    MessageFormat.format(EncodeError.EXCEEDED_TYPE_SIZE_LIMIT.getMessage(), UInteger8Type.class.getName()));
        }

        datagram[byteOffset] = (byte) value;
    }
}
