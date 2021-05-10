package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.ByteType;

public class ByteDecoder implements TypeDecoder<Byte> {
    @Override
    public Byte decode(DecodeContext context) {
        ByteType type = context.getDataType(ByteType.class);

        return this.decode(context.getDatagram(), type.byteOffset());
    }

    public byte decode(final byte[] datagram, int byteOffset) {
        if (datagram.length - ByteType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return datagram[byteOffset];
    }
}
