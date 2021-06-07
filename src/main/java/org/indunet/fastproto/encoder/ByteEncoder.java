package org.indunet.fastproto.encoder;


import lombok.NonNull;
import org.indunet.fastproto.annotation.type.ByteType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * Byte type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,ByteType
 * @since 1.0.0
 */
public class ByteEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        ByteType type = context.getDataType(ByteType.class);
        Byte value = context.getValue(Byte.class);

        this.encode(context.getDatagram(), type.value(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, byte value) {
        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + ByteType.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        datagram[byteOffset] = value;
    }
}
