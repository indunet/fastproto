package org.indunet.fastproto.encoder;


import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.BooleanType;

import java.lang.annotation.Annotation;

public class BooleanEncoder implements Encoder<Boolean> {
    public final static int BIT_OFFSET_MAX = 7;
    public final static int BIT_OFFSET_MIN = 0;

    @Override
    public void encode(byte[] datagram, EndianPolicy endian, Annotation dataTypeAnnotation, Boolean value) {
        int byteOffset = ((BooleanType) dataTypeAnnotation).byteOffset();
        int bitOffset = ((BooleanType) dataTypeAnnotation).bitOffset();

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
