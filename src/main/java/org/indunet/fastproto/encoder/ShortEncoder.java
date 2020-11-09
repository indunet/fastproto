package org.indunet.fastproto.encoder;

import org.vnet.fastproto.Endian;
import org.vnet.fastproto.ProtoPolicy;
import org.vnet.fastproto.annotation.*;
import org.vnet.fastproto.exception.EncodeException;

import java.lang.reflect.Field;

public class ShortEncoder implements NumberEncoder<Integer> {
    @Override
    public void set(byte[] datagram, int byteOffset, Endian endian, Integer value) throws EncodeException {
        if (value > Integer16Type.MAX || value < Integer16Type.MIN) {
            throw new EncodeException("The value is too large, please select the appropriate encoder.");
        } else if (datagram.length - Integer16Type.SIZE < byteOffset) {
            throw new EncodeException("Insufficient datagram space.");
        }

        if (endian == Endian.Little) {
            datagram[byteOffset] = (byte)(value.shortValue() & 0xFF);
            datagram[byteOffset + 1] = (byte)(value.shortValue() >> 8 & 0xFF);
        } else if (endian == Endian.Big) {
            datagram[byteOffset + 1] = (byte)(value.shortValue() & 0xFF);
            datagram[byteOffset] = (byte)(value.shortValue() >> 8 & 0xFF);
        }
    }

    @Override
    public void set(byte[] datagram, int byteOffset, Integer value) {
        this.set(datagram, byteOffset, Endian.Little, value);
    }

    @Override
    public void set(byte[] datagram, Object object, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(Integer16Type.class) == false) {
            return;
        } else if (field.isAnnotationPresent(EncodeIgnore.class)) {
            return;
        } else if (field.getAnnotation(Integer16Type.class).policy() == ProtoPolicy.Decode_Only) {
            return;
        }

        Endian endian;

        if (field.isAnnotationPresent(EndianMode.class)) {
            endian = field.getAnnotation(EndianMode.class).value();
        } else if (object.getClass().isAnnotationPresent(EndianMode.class)) {
            endian = object.getClass().getAnnotation(EndianMode.class).value();
        } else {
            endian = Endian.Little;
        }

        int byteOffset = field.getAnnotation(Integer16Type.class).value();
        int value = field.getInt(object);

        this.set(datagram, byteOffset, endian, value);
    }
}
