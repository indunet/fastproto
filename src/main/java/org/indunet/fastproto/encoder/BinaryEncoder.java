package org.indunet.fastproto.encoder;

import org.vnet.fastproto.ProtoPolicy;
import org.vnet.fastproto.annotation.ByteArrayType;
import org.vnet.fastproto.annotation.EncodeIgnore;
import org.vnet.fastproto.exception.EncodeException;

import java.lang.reflect.Field;

public class BinaryEncoder implements ByteArrayEncoder {

    @Override
    public void set(byte[] datagram, int byteOffset, byte[] value) throws EncodeException {
        if (datagram.length - value.length < byteOffset) {
            throw new EncodeException("Insufficient datagram space.");
        }

        for (int i = 0; i < value.length; i ++) {
            datagram[byteOffset + i] = value[i];
        }
    }

    @Override
    public void set(byte[] datagram, Object object, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(ByteArrayType.class) == false) {
            return;
        } else if (field.isAnnotationPresent(EncodeIgnore.class)) {
            return;
        } else if (field.getAnnotation(ByteArrayType.class).policy() == ProtoPolicy.Decode_Only) {
            return;
        }

        int byteOffset = field.getAnnotation(ByteArrayType.class).byteOffset();
        // int length = field.getAnnotation(ByteArrayType.class).length();
        byte[] value = ((byte[]) field.get(object));

        this.set(datagram, byteOffset, value);
    }
}
