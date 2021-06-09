package org.indunet.fastproto.encoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.text.MessageFormat;

/**
 * UInteger32 type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,UInteger32Type
 * @since 1.2.0
 */
public class UInteger32Encoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        UInteger32Type type = context.getDataType(UInteger32Type.class);
        EndianPolicy policy = context.getEndianPolicy();
        Long value = context.getValue(Long.class);

        this.encode(context.getDatagram(), type.value(), policy, value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, @NonNull EndianPolicy policy, long value) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + UInteger32Type.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        } else if (value > UInteger32Type.MAX_VALUE || value < UInteger32Type.MIN_VALUE) {
            throw new EncodeException(
                    MessageFormat.format(EncodeError.EXCEEDED_TYPE_SIZE_LIMIT.getMessage(), UInteger32Type.class.getName()));
        }

        if (policy == EndianPolicy.BIG) {
            datagram[byteOffset + 3] = (byte) (value & 0xFF);
            datagram[byteOffset + 2] = (byte) (value >> 8 & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 16 & 0xFF);
            datagram[byteOffset] = (byte) (value >> 24 & 0xFF);
        } else {
            datagram[byteOffset] = (byte) (value & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFF);
            datagram[byteOffset + 2] = (byte) (value >> 16 & 0xFF);
            datagram[byteOffset + 3] = (byte) (value >> 24 & 0xFF);
        }
    }
}
