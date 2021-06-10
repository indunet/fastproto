package org.indunet.fastproto.encoder;


import org.indunet.fastproto.annotation.type.BooleanType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * Boolean type encoder.
 *
 * @author Deng Ran
 * @since 1.0.0
 * @see TypeEncoder,BooleanType
 */
public class BooleanEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        BooleanType type = context.getTypeAnnotation(BooleanType.class);
        Boolean value = context.getValue(Boolean.class);

        this.encode(context.getDatagram(), type.value(), type.bitOffset(), value);
    }

    public void encode(byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset >= datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        } else if (bitOffset < BooleanType.MIN_BIT_OFFSET || bitOffset > BooleanType.MAX_BIT_OFFSET) {
            throw new EncodeException(EncodeError.ILLEGAL_BIT_OFFSET);
        }

        if (value) {
            datagram[byteOffset] |= (0x01 << bitOffset);
        } else {
            datagram[byteOffset] &= ~(0x01 << bitOffset);
        }
    }
}
