package org.indunet.fastproto.encoder;


import org.indunet.fastproto.annotation.type.BooleanType;

public class BooleanEncoder implements TypeEncoder<Boolean> {
    @Override
    public void encode(EncodeContext<Boolean> context) {
        BooleanType type = context.getDataType(BooleanType.class);
        Boolean value = context.getValue();

        this.encode(context.getDatagram(), type.byteOffset(), type.bitOffset(), value);
    }

    public void encode(byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        if (datagram.length <= byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        } else if (bitOffset < BooleanType.MIN_BIT_OFFSET || bitOffset > BooleanType.MAX_BIT_OFFSET) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (value) {
            datagram[byteOffset] |= (0x01 << bitOffset);
        } else {
            datagram[byteOffset] &= ~(0x01 << bitOffset);
        }
    }
}
