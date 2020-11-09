package org.indunet.fastproto.encoder;


import org.indunet.fastproto.annotation.BooleanType;

public class BooleanEncoder implements Encoder {
    public final static int BIT_OFFSET_MAX = 7;
    public final static int BIT_OFFSET_MIN = 0;

    @Override
    public void encode(EncodeContext context) {
        byte[] datagram = context.getDatagram();
        int byteOffset = context.getDataTypeAnnotation(BooleanType.class).byteOffset();
        int bitOffset = context.getDataTypeAnnotation(BooleanType.class).bitOffset();
        boolean value = context.getValue(Boolean.class);

        this.encode(datagram, byteOffset, bitOffset, value);
    }

    public void encode(byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        if (datagram.length <= byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        } else if (bitOffset < BIT_OFFSET_MIN || bitOffset > BIT_OFFSET_MAX) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (value) {
            datagram[byteOffset] |= (0x01 << bitOffset);
        } else {
            datagram[byteOffset] &= ~(0x01 << bitOffset);
        }
    }
}
