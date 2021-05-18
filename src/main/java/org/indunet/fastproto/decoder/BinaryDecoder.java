package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.BinaryType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

public class BinaryDecoder implements TypeDecoder<byte[]> {
    @Override
    public byte[] decode(DecodeContext context) {
        BinaryType type = context.getDataType(BinaryType.class);

        return this.decode(context.getDatagram(), type.byteOffset(), type.length());
    }

    public byte[] decode(final byte[] datagram, int byteOffset, int length) {
        if (datagram.length - length < byteOffset) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE, new ArrayIndexOutOfBoundsException());
        }

        byte[] bytes = new byte[length];

        System.arraycopy(datagram, byteOffset, bytes, 0, length);
        return bytes;
    }
}
