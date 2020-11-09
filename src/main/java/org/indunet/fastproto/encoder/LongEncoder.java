package org.indunet.fastproto.encoder;

import org.vnet.fastproto.Endian;
import org.vnet.fastproto.ProtoPolicy;
import org.vnet.fastproto.annotation.*;
import org.vnet.fastproto.exception.EncodeException;

import java.lang.reflect.Field;

public class LongEncoder implements NumberEncoder<Long> {
    @Override
    public void set(byte[] datagram, int byteOffset, Endian endian, Long value) throws EncodeException {
        if (datagram.length - Integer64Type.SIZE < byteOffset) {
            throw new EncodeException("Insufficient datagram space.");
        }

        if (endian == Endian.Little) {
            datagram[byteOffset] = (byte)(value & 0xFFL);
            datagram[byteOffset + 1] = (byte)(value >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte)(value >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte)(value >> 24 & 0xFFL);
            datagram[byteOffset + 4] = (byte)(value >> 32 & 0xFFL);
            datagram[byteOffset + 5] = (byte)(value >> 40 & 0xFFL);
            datagram[byteOffset + 6] = (byte)(value >> 48 & 0xFFL);
            datagram[byteOffset + 7] = (byte)(value >> 56 & 0xFFL);
        } else if (endian == Endian.Big) {
            datagram[byteOffset + 7] = (byte)(value & 0xFFL);
            datagram[byteOffset + 6] = (byte)(value >> 8 & 0xFFL);
            datagram[byteOffset + 5] = (byte)(value >> 16 & 0xFFL);
            datagram[byteOffset + 4] = (byte)(value >> 24 & 0xFFL);
            datagram[byteOffset + 3] = (byte)(value >> 32 & 0xFFL);
            datagram[byteOffset + 2] = (byte)(value >> 40 & 0xFFL);
            datagram[byteOffset + 1] = (byte)(value >> 48 & 0xFFL);
            datagram[byteOffset] = (byte)(value >> 56 & 0xFFL);
        }
    }

    @Override
    public void set(byte[] datagram, int byteOffset, Long value) {
        this.set(datagram, byteOffset, Endian.Little, value);
    }

    @Override
    public void set(byte[] datagram, Object object, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(Integer64Type.class) == false) {
            return;
        } else if (field.isAnnotationPresent(EncodeIgnore.class)) {
            return;
        } else if (field.getAnnotation(Integer64Type.class).policy() == ProtoPolicy.Decode_Only) {
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

        int byteOffset = field.getAnnotation(Integer64Type.class).value();
        long value = field.getLong(object);

        this.set(datagram, byteOffset, endian, value);
    }
}
