package org.indunet.fastproto.encoder;


import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.ByteType;

import java.lang.annotation.Annotation;

public class ByteEncoder implements Encoder<Byte> {
    @Override
    public void encode(byte[] datagram, EndianPolicy endian, Annotation dataTypeAnnotation, Byte value) {
        int byteOffset = ((ByteType) dataTypeAnnotation).byteOffset();

        if (byteOffset + ByteType.SIZE >= datagram.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        datagram[byteOffset] = value;
    }

    public void encode(byte[] datagram, int byteOffset, byte value) {
        if (byteOffset + ByteType.SIZE >= datagram.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        datagram[byteOffset] = value;
    }
}
