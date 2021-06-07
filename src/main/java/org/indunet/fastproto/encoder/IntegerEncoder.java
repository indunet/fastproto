package org.indunet.fastproto.encoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * Integer type encoder.
 *
 * @author Deng Ran
 * @since 1.0.0
 * @see TypeEncoder,IntegerType
 */
public class IntegerEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        IntegerType type = context.getDataType(IntegerType.class);
        Integer value = context.getValue(Integer.class);

        this.encode(context.getDatagram(), type.value(), context.getEndianPolicy(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, @NonNull EndianPolicy policy, int value) {
        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + IntegerType.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (policy == EndianPolicy.LITTLE) {
            datagram[byteOffset] = (byte) (value & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFF);
            datagram[byteOffset + 2] = (byte) (value >> 16 & 0xFF);
            datagram[byteOffset + 3] = (byte) (value >> 24 & 0xFF);
        } else if (policy == EndianPolicy.BIG) {
            datagram[byteOffset + 3] = (byte) (value & 0xFF);
            datagram[byteOffset + 2] = (byte) (value >> 8 & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 16 & 0xFF);
            datagram[byteOffset] = (byte) (value >> 24 & 0xFF);
        }
    }
}
