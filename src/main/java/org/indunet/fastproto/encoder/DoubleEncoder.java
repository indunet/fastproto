package org.indunet.fastproto.encoder;

import org.vnet.fastproto.Endian;
import org.vnet.fastproto.ProtoPolicy;
import org.vnet.fastproto.annotation.*;
import org.vnet.fastproto.exception.EncodeException;

import java.lang.reflect.Field;

public class DoubleEncoder implements NumberEncoder<Double> {
    @Override
    public void set(byte[] datagram, int byteOffset, Endian endian, Double value) throws EncodeException {
        long bits = Double.doubleToLongBits(value);

        if (endian == Endian.Little) {
            datagram[byteOffset] = (byte)(bits & 0xFFL);
            datagram[byteOffset + 1] = (byte)(bits >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte)(bits >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte)(bits >> 24 & 0xFFL);
            datagram[byteOffset + 4] = (byte)(bits >> 32 & 0xFFL);
            datagram[byteOffset + 5] = (byte)(bits >> 40 & 0xFFL);
            datagram[byteOffset + 6] = (byte)(bits >> 48 & 0xFFL);
            datagram[byteOffset + 7] = (byte)(bits >> 56 & 0xFFL);
        } else if (endian == Endian.Big) {
            datagram[byteOffset + 7] = (byte)(bits & 0xFFL);
            datagram[byteOffset + 6] = (byte)(bits >> 8 & 0xFFL);
            datagram[byteOffset + 5] = (byte)(bits >> 16 & 0xFFL);
            datagram[byteOffset + 4] = (byte)(bits >> 24 & 0xFFL);
            datagram[byteOffset + 3] = (byte)(bits >> 32 & 0xFFL);
            datagram[byteOffset + 2] = (byte)(bits >> 40 & 0xFFL);
            datagram[byteOffset + 1] = (byte)(bits >> 48 & 0xFFL);
            datagram[byteOffset] = (byte)(bits >> 56 & 0xFFL);
        }
    }

    @Override
    public void set(byte[] datagram, int byteOffset, Double value) {
        this.set(datagram, byteOffset, Endian.Little, value);
    }

    @Override
    public void set(byte[] datagram, Object object, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(DoubleType.class) == false) {
            return;
        } else if (field.isAnnotationPresent(EncodeIgnore.class)) {
            return;
        } else if (field.getAnnotation(DoubleType.class).policy() == ProtoPolicy.Decode_Only) {
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

        int byteOffset = field.getAnnotation(DoubleType.class).value();
        double value = field.getInt(object);

        this.set(datagram, byteOffset, endian, value);
    }
}
