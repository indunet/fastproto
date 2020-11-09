package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.ByteType;

public class ByteDecoder implements Decoder<Byte> {
    @Override
    public Byte decode(DecodeContext context) {
        byte[] datagram = context.getDatagram();
        int byteOffset = context.getDataTypeAnnotation(ByteType.class).byteOffset();

        return this.decode(datagram, byteOffset);
    }

    public byte decode(final byte[] datagram, int byteOffset) {
        if (datagram.length - ByteType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return datagram[byteOffset];
    }
}
