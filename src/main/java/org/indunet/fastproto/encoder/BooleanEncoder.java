package org.indunet.fastproto.encoder;

import org.vnet.fastproto.ProtoPolicy;
import org.vnet.fastproto.annotation.BooleanType;
import org.vnet.fastproto.annotation.EncodeIgnore;
import org.vnet.fastproto.exception.EncodeException;

import java.lang.reflect.Field;

public class BooleanEncoder implements BooleanEncoder<Boolean> {
    public final static int BIT_OFFSET_MAX = 7;
    public final static int BIT_OFFSET_MIN = 0;

    @Override
    public void set(byte[] datagram, int byteOffset, int bitOffset, Boolean value) throws EncodeException {
        if (datagram.length <= byteOffset) {
            throw new EncodeException("Insufficient datagram space.");
        } else if (bitOffset < BIT_OFFSET_MIN || bitOffset > BIT_OFFSET_MAX) {
            throw new EncodeException("Invalid bit offset.");
        }

        if (value) {
            datagram[byteOffset] |= (0x01 << bitOffset);
        } else {
            datagram[byteOffset] &= ~(0x01 << bitOffset);
        }
    }

    @Override
    public void set(byte[] datagram, Object object, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(BooleanType.class) == false) {
            return;
        } else if (field.isAnnotationPresent(EncodeIgnore.class)) {
            return;
        } else if (field.getAnnotation(BooleanType.class).policy() == ProtoPolicy.Decode_Only) {
            return;
        }

        int byteOffset = field.getAnnotation(BooleanType.class).byteOffset();
        int bitOffset = field.getAnnotation(BooleanType.class).bitOffset();
        boolean value = field.getBoolean(object);

        this.set(datagram, byteOffset, bitOffset, value);
    }
}
