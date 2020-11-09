package org.indunet.fastproto.decoder;

import org.vnet.fastproto.Endian;
import org.vnet.fastproto.ProtoPolicy;
import org.vnet.fastproto.annotation.DecodeIgnore;
import org.vnet.fastproto.annotation.Integer8Type;
import org.vnet.fastproto.exception.DecodeException;

import java.lang.reflect.Field;

public class ByteDecoder implements NumberDecoder<Integer> {
    @Override
    public Integer get(final byte[] datagram, int byteOffset, Endian endian) throws DecodeException {
        if (datagram.length - Integer8Type.SIZE < byteOffset) {
            throw new DecodeException("Insufficient datagram space.");
        }

        return (int)datagram[byteOffset];
    }

    // It doesn't matter of little endian or big endian.
    @Override
    public Integer get(final byte[] datagram, int byteOffset) {
        return this.get(datagram, byteOffset, Endian.Little);
    }

    @Override
    public void get(final byte[] datagram, Object object, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(Integer8Type.class) == false) {
            return;
        } else if (field.isAnnotationPresent(DecodeIgnore.class)) {
            return;
        } else if (field.getAnnotation(Integer8Type.class).policy() == ProtoPolicy.Encode_Only) {
            return;
        }

        int byteOffset = field.getAnnotation(Integer8Type.class).value();
        int value = this.get(datagram, byteOffset);

        field.setAccessible(true);
        field.setInt(object, value);
    }
}
