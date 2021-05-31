package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.CharacterType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * @author Deng Ran
 * @see TypeEncoder
 * @since 1.1.0
 */
public class CharacterEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        CharacterType type = context.getDataType(CharacterType.class);
        char value = context.getValue(Character.class);
        EndianPolicy policy = context.getEndianPolicy();

        this.encode(context.getDatagram(), type.value(), policy, value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy policy, char value) {
        if (byteOffset + CharacterType.size > datagram.length) {
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
