package org.indunet.fastproto.encoder;


import org.indunet.fastproto.annotation.type.ByteType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * Byte type encoder.
 *
 * @author Deng Ran
 * @since 1.0.0
 * @see TypeEncoder
 */
public class ByteEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        ByteType type = context.getDataType(ByteType.class);
        Byte value = context.getValue(Byte.class);

        this.encode(context.getDatagram(), type.value(), value);
    }

    public void encode(byte[] datagram, int byteOffset, byte value) {
        if (byteOffset + ByteType.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        datagram[byteOffset] = value;
    }
}
