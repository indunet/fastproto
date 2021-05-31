package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.ByteType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

/**
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.0.0
 */
public class ByteDecoder implements TypeDecoder<Byte> {
    @Override
    public Byte decode(DecodeContext context) {
        ByteType type = context.getDataType(ByteType.class);

        return this.decode(context.getDatagram(), type.value());
    }

    public byte decode(final byte[] datagram, int byteOffset) {
        if (byteOffset + ByteType.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        return datagram[byteOffset];
    }
}
