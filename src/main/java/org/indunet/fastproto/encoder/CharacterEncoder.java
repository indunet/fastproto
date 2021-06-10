package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.CharacterType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * Character type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder,CharacterType
 * @since 1.1.0
 */
public class CharacterEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        CharacterType type = context.getTypeAnnotation(CharacterType.class);
        Character value = context.getValue(Character.class);
        EndianPolicy policy = context.getEndianPolicy();

        this.encode(context.getDatagram(), type.value(), policy, value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy policy, char value) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + CharacterType.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (policy == EndianPolicy.BIG) {
            datagram[byteOffset] |= (value >> 8 & 0xFF);
            datagram[byteOffset + 1] |= (value & 0xFF);
        } else {
            datagram[byteOffset + 1] |= (value >> 8 & 0xFF);
            datagram[byteOffset] |= (value & 0xFF);
        }
    }
}
