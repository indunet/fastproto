package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.ShortType;

import java.lang.annotation.Annotation;

public class ShortEncoder implements TypeEncoder<Short> {
    @Override
    public void encode(EncodeContext<Short> context) {
        ShortType type = context.getDataType(ShortType.class);
        Short value = context.getValue();

        this.encode(context.getDatagram(), type.byteOffset(), context.getEndianPolicy(), value);
    }

    public void encode(byte[] datagram, int byteOffset, EndianPolicy policy, short value) {
        if (datagram.length - ShortType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (policy == EndianPolicy.Little) {
            datagram[byteOffset] = (byte) (value & 0xFF);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFF);
        } else if (policy == EndianPolicy.Big) {
            datagram[byteOffset + 1] = (byte) (value & 0xFF);
            datagram[byteOffset] = (byte) (value >> 8 & 0xFF);
        }
    }
}
