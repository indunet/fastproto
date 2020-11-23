package org.indunet.fastproto.encoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.BinaryType;

import java.lang.annotation.Annotation;

public class BinaryEncoder implements Encoder<byte[]> {
    @Override
    public void encode(byte[] datagram, Endian endian, Annotation dataTypeAnnotation, byte[] values) {
        int byteOffset = ((BinaryType) dataTypeAnnotation).byteOffset();

        this.encode(datagram, byteOffset, values);
    }

    public void encode(byte[] datagram, int byteOffset, byte[] values) {
        if (datagram.length - values.length < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        System.arraycopy(values, 0, datagram, byteOffset, values.length);
    }
}
