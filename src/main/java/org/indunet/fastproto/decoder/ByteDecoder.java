package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.ByteType;

import java.lang.annotation.Annotation;

public class ByteDecoder implements Decoder<Byte> {
    @Override
    public Byte decode(final byte[] datagram, Endian endian, Annotation dataTypeAnnotation) {
        int byteOffset = ((ByteType) dataTypeAnnotation).byteOffset();

        return this.decode(datagram, byteOffset);
    }

    public byte decode(final byte[] datagram, int byteOffset) {
        if (datagram.length - ByteType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return datagram[byteOffset];
    }
}
