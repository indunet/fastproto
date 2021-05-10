package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.BooleanType;

public class BooleanDecoder implements TypeDecoder<Boolean> {
    @Override
    public Boolean decode(DecodeContext context) {
        BooleanType type = context.getDataType(BooleanType.class);

        return this.decode(context.getDatagram(), type.byteOffset(), type.bitOffset());
    }

    public boolean decode(final byte[] datagram, int byteOffset, int bitOffset) {
        if (datagram.length <= byteOffset) {
            // TODO
            throw new ArrayIndexOutOfBoundsException();
        }

        return (datagram[byteOffset] & (0x01 << bitOffset)) != 0;
    }
}
