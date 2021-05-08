package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.BooleanType;

public class BooleanDecoder implements Decoder<Boolean> {
    @Override
    public Boolean decode(DecodeContext context) {
        int byteOffset = ((BooleanType) context.getDateType()).byteOffset();
        int bitOffset = ((BooleanType) context.getDateType()).bitOffset();

        return this.decode(context.getDatagram(), byteOffset, bitOffset);
    }

    public boolean decode(final byte[] datagram, int byteOffset, int bitOffset) {
        if (datagram.length <= byteOffset) {
            // TODO
            throw new ArrayIndexOutOfBoundsException();
        }

        return (datagram[byteOffset] & (0x01 << bitOffset)) != 0;
    }
}
