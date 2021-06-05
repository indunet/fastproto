package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.CharacterType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * Character type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,CharacterType
 * @since 1.1.0
 */
public class CharacterDecoder implements TypeDecoder<Character> {
    @Override
    public Character decode(DecodeContext context) {
        CharacterType type = context.getDataType(CharacterType.class);
        EndianPolicy policy = context.getEndianPolicy();

        return this.decode(context.getDatagram(), type.value(), policy);
    }

    public Character decode(final byte[] datagram, int byteOffset, @NonNull EndianPolicy policy) {
        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + CharacterType.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        int value = 0;

        if (policy == EndianPolicy.BIG) {
            value = (datagram[byteOffset] & 0xFF) * 256 + (datagram[byteOffset + 1] & 0xFF);
        } else {
            value = (datagram[byteOffset + 1] & 0xFF) * 256 + (datagram[byteOffset] & 0xFF);
        }

        return (char) value;
    }
}
