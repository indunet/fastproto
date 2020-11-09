package org.indunet.fastproto.encoder;

import org.vnet.fastproto.Endian;
import org.vnet.fastproto.ProtoPolicy;
import org.vnet.fastproto.annotation.EncodeIgnore;
import org.vnet.fastproto.annotation.EndianMode;
import org.vnet.fastproto.annotation.TimestampType;
import org.vnet.fastproto.exception.EncodeException;

import java.lang.reflect.Field;
import java.sql.Timestamp;

public class TimestampEncoder implements TimestampEncoder {
    protected org.vnet.fastproto.encoder.StandardUIntegerEncoder uIntegerEncoder = new org.vnet.fastproto.encoder.StandardUIntegerEncoder();
    protected org.vnet.fastproto.encoder.StandardLongEncoder longEncoder = new org.vnet.fastproto.encoder.StandardLongEncoder();

    @Override
    public void set(byte[] datagram, int byteOffset, TimestampType.Unit unit, Endian endian, Timestamp timestamp) {
        if (datagram.length - unit.getLength() < byteOffset) {
            throw new EncodeException("Insufficient datagram space.");
        }

        if (unit == TimestampType.Unit.Four_Bytes_Second) {
            long value = timestamp.getTime();

            this.uIntegerEncoder.set(datagram, byteOffset, endian, value / 1000);
        } else if (unit == TimestampType.Unit.Eight_Bytes_Second) {
            long value = timestamp.getTime() / 1000;

            this.longEncoder.set(datagram, byteOffset, endian, value);
        } else if (unit == TimestampType.Unit.Eight_Bytes_Millisecond) {
            long value = timestamp.getTime();

            this.longEncoder.set(datagram, byteOffset, endian, value);
        }
    }

    @Override
    public void set(byte[] datagram, Object object, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(TimestampType.class) == false) {
            return;
        } else if (field.isAnnotationPresent(EncodeIgnore.class)) {
            return;
        } else if (field.getAnnotation(TimestampType.class).policy() == ProtoPolicy.Decode_Only) {
            return;
        }

        Endian endian;

        if (object.getClass().isAnnotationPresent(EndianMode.class)) {
            endian = object.getClass().getAnnotation(EndianMode.class).value();
        } else if (field.isAnnotationPresent(EndianMode.class)) {
            endian = object.getClass().getAnnotation(EndianMode.class).value();
        } else {
            endian = Endian.Little;
        }

        int byteOffset = field.getAnnotation(TimestampType.class).byteOffset();
        TimestampType.Unit unit = field.getAnnotation(TimestampType.class).unit();

        Timestamp value = ((Timestamp) field.get(object));
        this.set(datagram, byteOffset, unit, endian, value);
    }
}
