package org.indunet.fastproto.encoder;

import org.vnet.fastproto.Endian;
import org.vnet.fastproto.ProtoPolicy;
import org.vnet.fastproto.annotation.*;
import org.vnet.fastproto.exception.EncodeException;

import java.lang.reflect.Field;

public class ByteEncoder implements NumberEncoder<Integer> {
    @Override
    public void set(byte[] datagram, int byteOffset, Endian endian, Integer value) throws EncodeException {
        if (value > Integer8Type.MAX || value < Integer8Type.MIN) {
            throw new EncodeException("The value is too large or too small.");
        } else if (datagram.length - Integer8Type.SIZE < byteOffset) {
            throw new EncodeException("Insufficient datagram space.");
        }

        datagram[byteOffset] = value.byteValue();
    }

    // It doesn't matter of little endian or big endian.
    @Override
    public void set(byte[] datagram, int byteOffset, Integer value) {
        this.set(datagram, byteOffset, Endian.Little, value);
    }

    @Override
    public void set(byte[] datagram, Object object, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(Integer8Type.class) == false) {
            return;
        } else if (field.isAnnotationPresent(EncodeIgnore.class)) {
            return;
        } else if (field.getAnnotation(Integer8Type.class).policy() == ProtoPolicy.Decode_Only) {
            return;
        }

        int byteOffset = field.getAnnotation(Integer8Type.class).value();
        int value = field.getInt(object);

        this.set(datagram, byteOffset, value);
    }
}
