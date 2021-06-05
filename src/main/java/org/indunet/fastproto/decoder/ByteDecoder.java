package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.annotation.type.ByteType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * Byte type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder,ByteType
 * @since 1.0.0
 */
public class ByteDecoder implements TypeDecoder<Byte> {
    @Override
    public Byte decode(DecodeContext context) {
        ByteType type = context.getDataType(ByteType.class);

        return this.decode(context.getDatagram(), type.value());
    }

    public byte decode(@NonNull final byte[] datagram, int byteOffset) {
        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + ByteType.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        return datagram[byteOffset];
    }
}
