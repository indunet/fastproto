package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.BinaryType;

import java.lang.annotation.Annotation;

public class BinaryDecoder implements Decoder<byte[]> {
    @Override
    public byte[] decode(final byte[] datagram, Endian endian, Annotation dataTypeAnnotation) {
        int byteOffset = ((BinaryType) dataTypeAnnotation).byteOffset();
        int length = ((BinaryType) dataTypeAnnotation).length();

        return this.decode(datagram, byteOffset, length);
    }

    public byte[] decode(final byte[] datagram, int byteOffset, int length) {
        if (datagram.length - length < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        byte[] bytes = new byte[length];

        System.arraycopy(datagram, byteOffset, bytes, 0, length);
        return bytes;
    }
}
