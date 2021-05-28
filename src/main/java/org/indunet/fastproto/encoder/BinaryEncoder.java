package org.indunet.fastproto.encoder;

import org.indunet.fastproto.annotation.type.BinaryType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

public class BinaryEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        BinaryType type = context.getDataType(BinaryType.class);
        byte[] bytes = context.getValue(byte[].class);

        this.encode(context.getDatagram(), type.byteOffset(), bytes);
    }

    public void encode(byte[] datagram, int byteOffset, byte[] bytes) {
        if (byteOffset + bytes.length >= datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        System.arraycopy(bytes, 0, datagram, byteOffset, bytes.length);
    }
}
