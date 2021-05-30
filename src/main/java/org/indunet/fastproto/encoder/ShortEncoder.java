package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.ShortType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * @author Deng Ran
 * @since 1.0.0
 * @see TypeEncoder
 */
public class ShortEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        ShortType type = context.getDataType(ShortType.class);
        Short value = context.getValue(Short.class);

        this.encode(context.getDatagram(), type.value(), context.getEndianPolicy(), value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy policy, short value) {
        if (datagram.length - ShortType.SIZE < byteOffset) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (policy == EndianPolicy.LITTLE) {
            datagram[byteOffset] = (byte) (value & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFF);
        } else if (policy == EndianPolicy.BIG) {
            datagram[byteOffset + 1] = (byte) (value & 0xFF);
            datagram[byteOffset] = (byte) (value >> 8 & 0xFF);
        }
    }
}
